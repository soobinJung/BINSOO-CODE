<template>
  <el-dialog
    show-close
    title         = "카테고리 변경"
    :visible.sync = "popupStatus"
    :before-close = "popupClose"
    width         = "1000px"
    top           = "2%"
  >
      <div style = "height: 60px; padding : 0 0 20px 0">
          <div style = "width: 10%; height: 40px; margin: 0 5% 0 0; padding: 10px 0 0 20px; text-align: left; float: left;">
              <span style = "font-size: 17px; font-weight: bold; ">검색어</span>
          </div>
          <div style = "width: 75%; height: 40px; float: left; margin: 0 5% 0 0;">
              <el-input 
                  placeholder         = "카테고리 명을 입력하세요." 
                  v-model             = "categorySearch"
                  style               = " width: 780px"
                  
                  @keyup.enter.native = "searchClick()"></el-input>
          </div>
      </div>
      <div>
        <div class="block-2nd-child">
          <el-tree
            draggable
            @node-contextmenu      = "loadCodeDetail"
            :filter-node-method    = "filterNode"
            :data                  = "treeData"
            :default-expanded-keys = "[1,2]"
            :expand-on-click-node  = "false"
            :render-content        = "renderContent"
            node-key               = "id"
            ref                    = "tree" 
            >
          </el-tree>
        </div>
      </div>
      <div slot = "footer" class = "dialog-footer">
          <el-button @click = "categoryUpdate" type = "primary" >변경</el-button>
          <el-button @click = "popupClose">취소</el-button>
      </div>
  </el-dialog>
</template>

<script>
import { getApiSystemList } from "@/api/poms-api/dictionary/pattern/upload/upload"

export default {
  name : 'PattonDictinary_PattonUpload_Patton_Set'

  , props:  {
      popupStatus: {
          type : Boolean
          , default : false
      }
  }
    
  , data() {
    const data = [{
      id          : 1
      , className : 'folder-home'
      , label     : ''
      , type      : 'root'
      , children  : []
    }];

    return {
      id                : 0
      , treeData        : JSON.parse(JSON.stringify(data))
      , treeDataTemp    : []
      , choiseCategory : {
        id               : 0
        , ic_name        : ''
        , ic_type        : ''
        , ic_code        : ''
        , ic_seq         : 0 
        , label          : ''
        , systemKey      : 0
        , weighted_point : 0
        , children       : []
      }
      , categorySearch    : ''
      , su_seq            : 10
      , categorySearchMap : new Map()
    }
  }

  , created () {
    this.getCodeTree()
  }

  , methods: {

    /** system And Category List **/
    async getCodeTree(){
      let param = {
        popup_chk : 1
      }
      
      await getApiSystemList(param).then((response) => {

      let list = []

        response.data.response_data.forEach( x => {
          list.push({
            id          : x.id
            , label     : x.label
            , className : x.className
            , systemKey : x.systemKey
            , type      : x.type
            , children  : x.children
          })
        })

        this.treeData.children = [];
        this.treeData[0].children = JSON.parse(JSON.stringify(list));
      });
    }
  

    /** 카테고리 변경 **/
    , categoryUpdate(){

      if(this.choiseCategory.id === 0){
        this.$alert('선택 된 카테고리가 없습니다.' )
        return
      }

      if( this.choiseCategory.children === null || this.choiseCategory.children === undefined ){
        this.categoryUpdateCheck(true)

      } else if ( this.choiseCategory.children.length === 0 ){
        this.categoryUpdateCheck(true)

      } else if ( this.choiseCategory.children.length > 0 ){
        this.categoryUpdateCheck(false)
        return
      }
    }

    , categoryUpdateCheck( val ){
      if(val){
        this.$confirm( '[ ' + this.choiseCategory.label +  ' ] 변경 하시겠습니까 ?.' ).then( _ => { 
          this.$emit("update:category", this.choiseCategory)
          this.popupClose()
        }).catch(_ => { return });
      }else{
        this.$alert( '[ ' + this.choiseCategory.label +  ' ] 카테고리는 선택 하실 수 없습니다.' )
      }
    }

    /** Tree 생성 **/
    , renderContent(h, { node, data, store }) {
      return (
        <div style = "width : 100%" on-click={ () => this.loadCodeDetail(data) }>
          <span class="custom-tree-node">
            <i class={data.className}></i>
            <span v-show={this.modifyBoxState==data.$treeNodeId} style="margin:5px;">
              <input id={data.$treeNodeId} type="text" v-model={data.label} />
            </span>
            <span v-show={this.modifyBoxState!=data.$treeNodeId} style="margin:5px;">{data.label}</span>
          </span>
        </div>);
    }

    /** 카테고리 선택 **/
    , loadCodeDetail(val){
      this.choiseCategory = {
        id               : val.id
        , ic_name        : val.ic_name
        , ic_type        : val.ic_type
        , ic_code        : val.ic_code
        , ic_ptype       : val.ic_ptype
        , ic_pcode       : val.ic_pcode
        , ic_seq         : val.ic_seq
        , label          : val.label
        , systemKey      : val.systemKey
        , weighted_point : val.weighted_point
        , children       : val.children
      }
    }

    /** popup 닫기 **/
    , popupClose() {
      this.$emit("close:popup", false)
    }

    /** Tree Search **/
    , filterNode(value, data) {
        if (!value) return true;
        return data.label.indexOf(value) !== -1;
    }
  }
    
  , watch : {
    categorySearch(val) {
      this.$refs.tree.filter(val);
    }
  }
};
</script>

<style scoped>

.el-tag::before {
content: "";
position: relative;
width: 20px;
height: 16px;
padding: 0px 4px;
color: #ffffff;
font-size: 14px;
font-weight: bold;
text-align: center;
line-height: 16px;
}

.el-tag--success::before {
content: 'D';
background: #81f8c1;
}

.el-tag--danger::before {
content: 'M';
background: #f881e8;
}

::v-deep .el-dialog { 
height: 750px; 
}

::v-deep .el-dialog__header {
position: relative;
height: 45px;
padding: 0 !important;
background: #1890ff;
}

::v-deep .el-dialog__title {
position: absolute;
height: auto !important;
top: 50%;
transform: translateY(-50%);
height: 24px;
padding: 0 0 0 20px;
color: #fff;
font-size: 16px;
}

::v-deep .el-dialog__headerbtn {
position: absolute;
top: 50%;
right: 0;
transform: translateY(-50%);
padding: 0 20px 0 0;
}

::v-deep .el-dialog__close {
color: #fff !important;
}

.dialog-title {
color: black;
}

.el-col {
border-radius: 4px;
}
.bg-purple {
border: 1px solid #ccc;
padding: 10px 20px;
height: 520px;
}

.bg-purple-light {
margin-left: 20px;
border: 1px solid #ccc;
padding: 10px 20px;
height: 520px;
}

.grid-content {
min-height: 36px;
}

.row-bg {
padding: 10px 0;
background-color: #f9fafc;
}

.scrollable {
height: 150px;
max-height: 100%;
overflow-y: auto !important;
display: block;
}

.infos {
font-size: 11px;
color: #204fa3;
line-height: 1.5;
font-weight: 600;
}

.grid-content > table {
width: 100%;
border-spacing: 0 10px;
table-layout: fixed;
border-style: hidden;
}

.grid-content > table > tr > td,
.grid-content > table > tr > th {
padding-bottom: 10px;
border-bottom: 1px solid #aaa;
}

::v-deep .el-checkbox__inner {
width: 20px !important;
height: 20px !important;
}

::v-deep .el-checkbox__inner::after {
height: 9px !important;
left: 6px !important;
top: 2px !important;
width: 5px !important;
}

::v-deep .el-input__inner {
overflow: hidden;
text-overflow: ellipsis;
white-space: nowrap;
}

::v-deep .el-form-item__content {
display: flex;
}

.el-tag + .el-tag {
margin-left: 10px;
}

.button-new-tag {
margin-left: 10px;
height: 32px;
line-height: 30px;
padding-top: 0;
padding-bottom: 0;
}

.input-new-tag {
width: 90px;
margin-left: 10px;
vertical-align: bottom;
} 

::v-deep .el-form-item__content {
display: block;
}

.info > ul {
list-style:none;
padding:10px 0 0 10px;
margin:0;
}

.info > ul >li {
color: #aaa;
position: relative;
padding: 0 0 3px 10px;
line-height: 1.2em;
font-size: 12px;
margin:0;
}

.info > ul > li::before{
content: '- ';
}

.block-2nd-child {
height: 500px;
overflow-y:scroll;
}
</style>
