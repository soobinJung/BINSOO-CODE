<template>
  <div class="app-container">
    <div class="filter-container">
      <table class="search-table">
        <tr>
          <td style="width:8%"> <h4>System</h4> </td>
          <td class="td3">
            <el-select filterable v-model="systemCode" style="width:100%" @change="getRootCode">
              <el-option
                v-for  = "item in systemCodeOptions"
                :key   = "item.value"
                :label = "item.label"
                :value = "item.value">
              </el-option>
            </el-select>
          </td>
        </tr>
      </table>
    </div>
  <el-row>
    <el-col :span="8">
      <div class = "grid-content bg-purple">
          <div class = "block" style = "box-sizing: border-box; overflow-y: auto; height: 100%; padding: 0 0 30px 0">
            <el-tree
              accordion
              :data                  = "treeData"
              :default-expanded-keys = "expandedKeys"
              :expand-on-click-node  = "false"
              :render-content        = "renderContent"
              :allow-drop            = "allowDrop"
              @node-contextmenu      = "loadCodeDetail"
              @node-expand           = "removeInput"
              node-key               = "id"
              v-loading              = "listLoading"
              draggable
              auto-expand-parent
              >
            </el-tree>
          </div>
      </div>
    </el-col>
    <el-col :span="16">
      <codeInfo 
        :codeDetail    = "codeDetail" 
        :listLoading2  = "listLoading2" 
        :systemCode    = "systemCode"
        v-show         = "this.codeInfoShow"
        @parent:render = "refreshFetchList"/>
    </el-col>
  </el-row> 
</div>
</template>

<script>
import _ from 'lodash'
import waves from "@/directive/waves";
import codeInfo from './component/codeInfo'
import { getSysCodeList , getRootCode, getCodeSeqList, getSearchCode, getMainCode, insertCode, deleteCode } from "@/api/poms-api/poms/code/code/code";
import { setPatchParam } from '@/common/common'

/** Root ID **/
let id = 1000;

export default {
  name: "Code-mng"
  , components: { codeInfo }
  , directives: { waves }
  , data() {
      /** Dummy Data */
      const codeData = [{id: 1, className: 'folder-home',label: 'Root',type: 'root', codeSeq : 0,  codeGroup:'', codeType:'-', codeValue : 0, selectedCode:'Root',  color:'none', codeColor: '', codeDepth : 0, textColor:'', clicked:false, children: [{id: 2,className: 'folder-opened', label: '채널', type: 'node1', color:'none', textColor:'', clicked:false, children: []}]}];
      const codeDetail = { selectedCode: '기본채널', company: 'DefaultChannel', codeGroup : '기본 설정', codeType : '기본 설정', codeName: '코드명 샘플', value: '코드값 샘플', color:'#FF1493', childCodeList : [] };
 
      return {
        systemCodeOptions  : []
        , systemCode : 0

        , treeData: JSON.parse(JSON.stringify(codeData))
        , codeDetail: JSON.parse(JSON.stringify(codeDetail))

        , codeInfoShow : ''
        , listLoading: true
        , listLoading2: true

        , expandedKeys : [1]

        , deleteList : []

        , appendState : {
          id : 0
          , status : false
          , nodeId : ''
        }
      }
 }

  , created() {
    this.created ()
  }

  , methods: {

    async created (){
      await this.setSystemCode()
      await this.loadCodeDetail( this.treeData[0])
    }

    , async codeNameOverLap ( codeName ){
      let param = {
          filedName : 'code_name'
          , value   : codeName
          , sysCode : this.systemCode
      }
      let result;
      await getMainCode(param).then( response => {
        result = (response.data.response_data.length > 0) ? false : true
      })
      return result
    }

    /** Code Insert **/
    , async codeInsert(node, data) { 
      var label = data.label.trim()

      if(label.length === 0){
        this.$alert('카테고리 명을 입력해주세요.')
        return
      }

      var parentId = this.appendState.id 
      var parent = this.findChildren( this.treeData, parentId )
    
      let overLap =  (parent.type === 'root') ? await this.codeNameOverLap(label) : true
      if(!overLap){ 
        this.$alert('이미 존재하는 코드명 입니다.')
        return
      }

      var param = {
        code_name       : label
        , code_p_seq    : (parent.type === 'root') ? 0 : parentId
        , code_p_value  : (parent.type === 'root') ? '' : parent.codeValue
        , code_type     : ''
        , code_group    : parent.codeGroup
        , ui_type       : 'tree'
        , child_use     : 'N'
        , code_depth    : (parent.type === 'root') ? 0 : (parent.codeDepth + 1)
        , code_sort_num : ( _.max( _.map( parent.children , 'codeSort')  ) + 1)
        , code_value    : ''
        , code_use      : 'Y'
        , code_color    : ''
        , code_etc      : ''
        , sys_code      : this.systemCode
      }

      await insertCode(param).then( response => {

        /** 저장된 Code 정보 Setting **/
        node.data = this.setOptions( response.data.response_data, 'tree')

        this.$notify({
          title   : "코드 추가",
          message : "코드 추가가 완료되었습니다.",
          type    : "success",
          duration: 2000
        });

        this.getRootCode()
      })
      this.appendState.nodeId = ''
    }

    /** Code Delete List **/
    , setDeleteList(childList){
      childList.forEach( x => {
        this.deleteList.push(x.code_seq)
        if(x.childList.length > 0){
          this.setDeleteList(x.childList)
        }
      })  
    }

    , async CodeDelete(codeSeq){
        var param = {
          codeSeq : codeSeq
        }
        await getCodeSeqList(param).then( response => {
          this.deleteList.push(codeSeq)
          this.setDeleteList(response.data.response_data[0].childList);
        })
        param = setPatchParam('code_use', 'N', 'code_seq', this.deleteList.join(','));

        await deleteCode(param).then ( response => {
          this.$notify({
              title    : "코드 삭제",
              message  : "코드 삭제가 완료되었습니다.",
              type     : "success",
              duration : 2000
          });
        })
    }

    /** Code Delete **/
    , async handleCodeDelete (node, data) {
      this.$confirm("삭제 하시겠습니까?").then((_) => {
        this.CodeDelete(data.codeSeq);
        const parent = node.parent
        const children = parent.data.children || parent.data
        const index = children.findIndex(d => d.id === data.id)
        children.splice(index, 1)
      })
      .catch((_) => {});
    }


    /** SysCode Setting **/
    , async setSystemCode(){
      await getSysCodeList().then( response => {
        response.data.response_data.forEach( x => {
          this.systemCodeOptions.push({ value : x.systemKey, label : x.system, system : x.system })
        })
        this.systemCode = (this.systemCodeOptions.length > 0) ? this.systemCodeOptions[0].value : 0
        this.getRootCode();
      })
    }


    /** Code Setting **/
    , setOptions (vo, uiType){
      var childList = []
      var childUi = vo.ui_type;

      if( vo.childList != null && vo.childList != undefined ){
        vo.childList.forEach( x => {
          childUi = x.ui_type
          if( x.ui_type === uiType ){
            childList.push( this.setOptions(x, uiType) )
          }
        })
      }

      return { 
        id           : vo.code_seq 
        , codeSeq    : vo.code_seq
        , label      : vo.code_name
        , codeName   : vo.code_name 
        , systemCode : vo.sys_code
        , className  : 'folder-opened'
        , type       : 'node1' 
        , children   : childList
        , codeGroup  : vo.code_group
        , codeColor  : (vo.code_color === null ) ? '#000000' : vo.code_color
        , codeType   : vo.code_type
        , codeValue  : vo.code_value
        , codeDepth  : vo.code_depth
        , codeSort   : vo.code_sort_num
        , uiType     : vo.ui_type
        , color      : 'none'
        , textColor  : ''
        , codeState  : false
        , childUi    : (childUi === 'tree') ? childUi : (childUi === 'val') ? childUi : childUi
      }
    }


    /** Root Code **/
    , async getRootCode(){

      var param = {
        depth : 1
        , sysCode : this.systemCode
      }

      await getRootCode(param).then( response => {
        this.listLoading = true
        var list = [];
        response.data.response_data.forEach( x => {
          if( x.ui_type === 'tree' ){
            list.push(this.setOptions(x, 'tree'))
          }
        })
        this.treeData[0].children = _.orderBy(list , ['codeSort', 'ASC'])
        this.listLoading = false
      })
    }


    /** Detail Info  */
    , async loadCodeDetail(thisNode){
      if(!thisNode){
         return;
      }else{
        this.codeInfoShow = thisNode;
        this.listLoading2 = false
      }

      var childCodeList = []
      this.removeInput();

      var isRoot = thisNode['type'] == 'root'
      /** Root == this **/
      if( isRoot ){

        var param = {
          sysCode     : this.systemCode
          , filedName : 'code_p_seq'
          , value     : 0
          , depth     : 1
        }

        await getSearchCode(param).then( response => {
            response.data.response_data.forEach( x => {
            var list = this.setOptions( x, 'val')
            if(list != undefined ){
              childCodeList.push( list )
            }
          })
        })
      /** Root != this **/
      }else{
        await getCodeSeqList(thisNode).then( response => {
          response.data.response_data.forEach( x => {
            x.childList.forEach( y => {
              var list = this.setOptions( y, 'val')
              if(list != undefined ){
                childCodeList.push( list )
              }
            })
          })
        })
      }

      /** Tree 정보 **/
      var codeDetail = {
        selectedCode    : thisNode.label || ''
        , codeSeq       : thisNode.codeSeq || 0
        , systemCode    : this.systemCode || 0
        , system        : _.find(this.systemCodeOptions, { value : this.systemCode}).system
        , codeGroup     : thisNode.codeGroup || ''
        , codeType      : thisNode.codeType || ''
        , value         : thisNode.codeValue || ''
        , type          : isRoot ? 'root' : 'nood'
        , color         : thisNode.codeColor || ''
        , codeDepth     : thisNode.codeDepth || 0
        , childCodeList : _.orderBy(childCodeList , ['codeSort', 'ASC'])
        , childUi       : thisNode.childUi || ''
      }
      this.codeDetail = codeDetail
    }


    /** Input Delete **/
    , removeInput(){
        if(document.querySelector('.tree-input') != null){
          document.querySelector('.tree-input').parentNode.parentNode.remove()
          var parent = this.findChildren(this.treeData, this.appendState.id)

          /** Parent 의 Children 에서 Delete **/
          _.remove( parent.children, function(x){
            return x.codeState === true
          })

          if(parent.children.length === 0){
            parent.children = []
          }
          this.appendState.id = 0
      }
    }


    /** list 에서 ID 찾기. 하위요소까지 탐색 **/
    , findChildren(list, id){
      if( list === undefined || id === undefined ){
        return 
      }
      var vo = { 
        id         : 0
        , label    : ''
        , children : [] 
      }

      list.forEach( x => {
        if (x.id === id ){
          vo = x
        }
        if(x.children.length > 0){
          var tempVo = this.findChildren( x.children , id )
          if(tempVo.id != 0 ){
            vo = tempVo
          }
        }
      })
      return vo
    }


    /** Node 추가 **/
    , async append(parent) {

      // let treeNodeId = parent.children[ parent.children.length - 1 ].$treeNodeId

      if( this.appendState.id != 0 ){
        this.removeInput()
      }

      /** 트리 추가 시도시 상위 트리 Folder Open **/
      this.expandedKeys = []
      this.expandedKeys.push(1, parent.id)

      let newChild = { 
        id          : id++
        , className : 'folder-opened'
        , label     : ''
        , type      : 'node'
        , codeSort  : 0
        , codeState : true
        , children  : [] 
      };

      if (!parent.children) {
        this.$set(parent, 'children', []);
      }
      await parent.children.push(newChild);

     
      let appendTreeNodeId = ( parent.children.length > 0 ) 
                                ? parent.children[ parent.children.length - 1 ].$treeNodeId
                                : 0

      /** 변경 중 Flag **/
      this.appendState.id     = parent.id
      this.appendState.nodeId = appendTreeNodeId

      this.$nextTick(_ => {
        var item = document.getElementById(appendTreeNodeId)
        if(!item) return;
        item.focus();
      })
    }
      

    , remove (node, data) {
      this.appendState.status = false
      const parent = node.parent
      const children = parent.data.children || parent.data
      const index = children.findIndex(d => d.id === data.id)
      children.splice(index, 1)
      this.appendState.nodeId = ''
    }
  
  
    , renderContent(h, { node, data, store }) {

      /** Root **/
      if(data.type === 'root'){

        /** NO 권한 **/
        if(this.authKind){
          return (
            <span class="custom-tree-node">
              <i class={data.className}  on-click={ () => this.loadCodeDetail(data) }></i>
              <span style="margin:5px;" on-click={ () => this.loadCodeDetail(data) }>{node.label}</span>
              <span>
                <el-button size="mini" type="text" on-click={ () => this.append(data) }>추가</el-button>
              </span>
            </span>);
            
        }else{
          return (
            <span class="custom-tree-node">
              <i class={data.className}  on-click={ () => this.loadCodeDetail(data) }></i>
              <span style="margin:5px;" on-click={ () => this.loadCodeDetail(data) }>{node.label}</span>
            </span>);
        }


      /** 입력 **/
      } else {
        if( this.appendState.nodeId == data.$treeNodeId ){
          this.appendState.status = true

          return (
            <span class="custom-tree-node tree-input">
              <i class={data.className}></i>
              <span style="margin:5px;"><input placeholder="코드명을 입력해주세요" id={data.$treeNodeId} type="text" v-model={data.label} on-keyup-enter={()=> this.codeInsert(node, data)}/></span>
              <span>
                <el-button size="mini" type="text" on-click={ () => this.codeInsert(node, data) } >등록</el-button>
                <el-button size="mini" type="text" on-click={ () => this.remove(node, data) }>취소</el-button>
              </span>
            </span>
          );


        /** 라벨 **/
        }else if(this.appendState.nodeId!=data.$treeNodeId){

          /** NO 권한 **/
          if(!this.authKind){
            return (
            <span class="custom-tree-node" id="parent">
              <i class={data.className} on-click={ () => this.loadCodeDetail(data) }></i>
              <span style="margin:5px;" on-click={ () => this.loadCodeDetail(data) }>{data.label}</span>
              <span style="text-align:right">
              </span>
            </span>
            );
          }

          if((data.childUi === 'tree') ){
            return (
            <span class="custom-tree-node" id="parent">
              <i class={data.className} on-click={ () => this.loadCodeDetail(data) }></i>
              <span style="margin:5px;" on-click={ () => this.loadCodeDetail(data) }>{data.label}</span>
              <span style="text-align:right">
                <el-button size="mini" type="text" on-click={ () => this.handleCodeDelete(node, data) }>삭제</el-button>
                <el-button class="child" id="child" size="mini" type="text" on-click={ () => this.append(data) } >추가</el-button>
              </span>
            </span>
            );

          }else{
            return (
            <span class="custom-tree-node" id="parent">
              <i class={data.className} on-click={ () => this.loadCodeDetail(data) }></i>
              <span style="margin:5px;" on-click={ () => this.loadCodeDetail(data) }>{data.label}</span>
              <span>
                <el-button size="mini" type="text" on-click={ () => this.handleCodeDelete(node, data) }>삭제</el-button>
              </span>
            </span>
            );
          }

        }else{
          return (<span></span>)
        }
      }
    }


    /** Root 밖으로는 못나가게 하고 그 안에서는 드래그 앤 드랍 가능 **/
    , allowDrop(draggingNode, dropNode, type){
      const typeRule = type === "prev" || type === "next"
      return (draggingNode.level > dropNode.level && dropNode.level === 1) ? !typeRule : typeRule;
    }


    , refreshFetchList(){
      this.getRootCode()
    }
  }
  , computed : {
    authKind () {
      return this.$route.meta.authKind === 'A' ? true : false
    }
  }
};
</script>

<style scoped>
/*  SEARCH BOX */
.search-table {
  font-size: 11.5pt;
  background-color: #ebeef3;
  padding: 10px 20px 10px 20px;
  width: 100%;
}

.aid-buttons {
  padding-top: 20px;
  text-align: center;
}

/* CONTENT BOX */
.block {
  border: 1px solid #DFE6EC;
  padding: 10px;
}

.block-right {
  border: 1px solid #DFE6EC;
}
.inform-table {
  width:100%;
  border: 1px solid #DFE6EC;
  border-collapse: collapse;
}

/* TREE ICON */

::v-deep .custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  font-size: 14px;
  padding-right: 10px;
}

::v-deep .folder-opened {
    top: 0;
    left: 0;
    width: 16px;
    height: 16px;
    background: url(../../../../assets/code-mng/ui_treeview.png) no-repeat -42px 0;
    background-position: -22px 0;
    text-align: left;
    text-indent: -9999px;
}

::v-deep .folder-home {
    top: 0;
    left: 0;
    width: 16px;
    height: 16px;
    background: url(../../../../assets/code-mng/ui_treeview.png) no-repeat 0px 0;
    text-align: left;
    text-indent: -9999px;
}

/* 세부 정보 */

.block-right > h3 {
  position: relative;
  padding-left: 20px;
  font-size: 16px;
  color: #202020;
  line-height: 32px;
}

.inform-table > tr > th {
  background: #F5F7FA;
}

.inform-table > tr > th, .inform-table > tr > td {
  border-right: 1px solid #DFE6EC;
  border-bottom: 1px solid #DFE6EC;
  padding: 12px;
}

.selectable {
  border: 1px solid #DFE6EC;
  padding: 0px;
  min-height: 300px;
}

.selectable-list {
  padding: 8px;
}


.selectable-list:hover{
  background: #497ac4 !important;
  color: white;
}

.selectable:hover{
  border: 1px solid #497ac4;
  padding: 0px;
  min-height: 300px;
}
.selectable:active {
  border: 1px solid #DFE6EC;
  padding: 0px;
  min-height: 300px;
}

.selectable-list:active {
  background: #a6bee2 !important;
  color: white;
}

.btn-manage {
  width: 80px;
  font-size: 14px;
  height: 35px;
  margin: 2px;
  border-radius: 0px;
}


/* TEMPORARY : 개발용 */

li {
  list-style:none;
}

</style>

<style lang="scss">

  .el-row {
    margin-top:10px;
    margin-bottom: 20px;
    &:last-child {
      margin-bottom: 0;
    }
  }
  .el-col {
    border-radius: 4px;
  }
  .bg-purple {
    padding: 10px;
  }
  .bg-purple-light {
    padding: 10px;
  }
  .grid-content {
    border-radius: 4px;
    min-height: 36px;
  }
  .row-bg {
    padding: 10px 0;
    background-color: #f9fafc;
  }

</style>
