<template>
  <el-dialog
    show-close
    title         = "카테고리 변경"
    :visible.sync = "popupStatus"
    :before-close = "popupClose"
    width         = "1000px"
    top           = "5%"
    append-to-body
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
import { getCategoryListBySystemKey } from '@/api/poms-api/poms/system/company-mng/company-mng' 

export default {
  name : 'System_Dictionary_Popup'

  , props:  {
      popupStatus: {
          type : Boolean
          , default : false
      }

      , info: {
          type : Object
          , default : function(){
              return {
                systemKey : 0
                , icType  : 0
              }
          }
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
      }
  }

  , created () {
    
  }

  , methods: {

    /** system And Category List **/
    async getCodeTree(){
      
      let param = {
        systemKey : this.info.systemKey
        , icType  : this.info.icType
      }
      
      await getCategoryListBySystemKey(param).then((response) => {
        if(response.data.response_data.length === 0 || response.data.response_data[0].children.length === 0){
          this.popupClose()
          this.$alert('카테고리가 없습니다.')
          return
        }
        
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
        this.categoryUpdateCheck()

      } else if ( this.choiseCategory.children.length === 0 ){
        this.categoryUpdateCheck()

      } else if ( this.choiseCategory.children.length > 0 ){
        this.$alert( '[ ' + this.choiseCategory.label +  ' ] 카테고리는 선택 하실 수 없습니다.' )
      }
    }

    , categoryUpdateCheck( ){
      this.$confirm( '[ ' + this.choiseCategory.label +  ' ] 변경 하시겠습니까 ?.' ).then( _ => {
        this.$emit("update:diccategory", this.choiseCategory)
        this.popupClose()
      }).catch(_ => { return });
    }

    /** Tree 생성 **/
    , renderContent(h, { node, data, store }) {
      return (
        <div style="width : 100%" on-click={ () => this.loadCodeDetail(data) }>
          <span class="custom-tree-node">
            <i class={data.className}></i>
            <span v-show={this.modifyBoxState==data.$treeNodeId} style="margin:5px;"><input id={data.$treeNodeId} type="text" v-model={data.label} vOn:Keyup_stop_prevent_enter={()=> this.editDone(node, data)} on-blur={()=> this.editDone(node, data)}/></span>
            <span v-show={this.modifyBoxState!=data.$treeNodeId} style="margin:5px;" >{data.label}</span>
          </span>
        </div>
       );
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
    categorySearch(value) {
      this.$refs.tree.filter(value);
    }

    , popupStatus (value){
      if(value){
        this.getCodeTree()
      }
    }
  }
};
</script>

<style scoped>
  ::v-deep .is-current > .el-tree-node__content {
     background: #1856ff;
     color: white;
   }
 /*  SEARCH BOX */
 .search-table {
   font-size: 11.5pt;
   background-color: #ebeef3;
   padding: 10px 20px 10px 20px;
   width: 100%;
 }
 .search-button {
     width:100px;
     height:36px;
 }
 
 </style>
 
 <style scoped>
 
 ::v-deep .el-message-box__container > .el-message-box__message > p {
     margin: 0;
     width: 330px;
     text-align: center;
     line-height: 24px;
 }
 .grid-content{
   padding : 10px
 }
 
 .block-2nd-child {
   padding: 10px 10px;
   height: 85%;
   overflow-y:scroll;
 }
 
 ::v-deep .el-tabs__nav{
   width:100%
 }
 
 ::v-deep .el-tabs--border-card > .el-tabs__header .el-tabs__item{
   width:51%;
   text-align: center;
 }
 
 .aid-buttons {
   padding-top: 20px;
   text-align: center;
 }
 
 /* CONTENT BOX */
 .block {
   border: 1px solid #DFE6EC;
 }
 .block-child{
   border: 1px solid #DFE6EC;
   padding: 10px;
   margin-bottom:10px;
   color:#606266;
   font-weight: bold;
   background: #DFE6EC;
 }
 
 .block-swipe-button{
   position: absolute;
   top: 10px;
   left: 380px;
 }
 .block-swipe-button-2nd{
   height: 100%;
   width: 10px;
 }
 
 .treeview_visible:before {
     content: '';
     position: absolute;
     top: 0;
     left: 0;
     width: 20px;
     height: 600px;
     border: 1px solid #d0d0d0;
     background: #d4d4d4;
     -webkit-box-sizing: border-box;
     box-sizing: border-box;
     z-index: 1;
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
   justify-content: start;
   font-size: 14px;
   padding-right: 10px;
 }
 
 /* ::v-deep .el-tree-node__content > span > span > .el-button {
   color:#606266 !important;
   opacity: 0;
 }
 ::v-deep .el-tree-node__content > span > span > .el-button:hover{
   color:#606266 !important;
   opacity: 1;
   font-weight: bold;
 } */
 
 
 ::v-deep .folder-opened {
     width: 16px;
     height: 16px;
     background: url(../../../../../../assets/code-mng/ui_icons.png) -31px -80px;
 }
 
 ::v-deep .folder-home {
     width: 21px;
     height: 18px;
     background: url(../../../../../../assets/code-mng/ui_icons.png) 0 -80px;
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
 
 <style lang="scss" scoped>
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
 </style>
 <style scope>
 
   .class_icon_tree::before {
     content: "";
     position: absolute;
     top: -8px;
     left: -14px;
     width: 16px;
     height: 16px;
     color: #ffffff;
     font-size: 12px;
     font-weight: bold;
     text-align: center;
     line-height: 16px;
   }
   .class_icon_tree {
     margin-left:15px;
     position: relative;
     display: inline-block;
     font-size: 14px;
     color:#606266;
     vertical-align: top;
     font-style: normal !important;
   }
   .dropdown-title {
     font-weight: bold;
     padding: 10px 0
   }
   .class_icon::before {
     content: "\00a0";
     display: block;
     position: absolute;
     left:-18px;
     width: 16px;
     height: 16px;
     color: #ffffff;
     font-size: 12px;
     font-weight: bold;
     text-align: center;
     line-height: 16px;
   }
   .class_icon {
     margin-left:15px;
     padding-right:10px;
     position: relative;
     display: inline-block;
     font-size: 14px;
     color:#606266;
     vertical-align: top;
   }
   .class_folder::before {
     width: 16px;
     height: 16px;
     background: url(../../../../../../assets/code-mng/ui_icons.png) -31px -80px;
   }
   .class1::before {
     content: 'C';
     border-radius: 50%;
     background: #81b1f8;
   }
   .class2::before {
     content: 'R';
     border-radius: 50%;
     background: #a26fef;
   }
   .class3::before {
     content: 'E';
     border-radius: 50%;
     background: #df5dbc;
   }
   .class4::before {
     content: 'S';
     border-radius: 50%;
     background: #df5d66;
   }
   .class5::before {
     content: 'M';
     border-radius: 0%;
     background: #f9feff;
     color:rgb(0, 0, 0);
   }
 
   .folder-opened-done {
     display: none;
   }
 
   .el-tree-content /deep/ .el-link{opacity: 0;}
 .el-tree-content /deep/ .el-link:last-child{margin-right: 60px;}
 .el-tree-content /deep/ .el-tree-node__content:hover .el-link{opacity: 1;}
 .el-tree--highlight-current{width: 100% !important; }
 
 .icon_info {
     display: block;
     position: relative;
     width: 58px;
     height: 58px;
     margin: 0 auto;
     border-radius: 50%;
     background: #e3edff;
 }
 
 .icon_info:after {
     content: '';
     position: absolute;
     top: 50%;
     left: 50%;
     width: 36px;
     height: 36px;
     margin: -18px 0 0 -18px;
     background: url(../../../../../../assets/code-mng/ui_alert.png) no-repeat 0 0;
 }
 
 .button-list {
   opacity: 0;
 }
 
 .custom-tree-node:hover .button-list{
   opacity: 1;
 }
 </style>
 