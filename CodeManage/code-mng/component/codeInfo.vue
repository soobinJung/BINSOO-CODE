<template>
<div class="grid-content bg-purple-light">
    <div class="block-right">
        <h3>세부정보</h3>
        <table class="inform-table" v-loading="listLoading2">
        <tr>
            <th colspan="2">
            선택코드
            </th>
            <td colspan="2">
            {{ codeDetail.selectedCode }}
            </td>
        </tr>
        <tr>
            <th colspan="2">
            코드그룹
            </th>
            <td v-if="codeDetail.type === 'root'" style="border-right:none">
              <span>-</span>
            </td>
            <td v-else-if="codeGroupStatus" style="border-right:none">
              <el-input
                v-model="codeDetail.codeGroup" 
                ref="codeGroup"
                size="mini"
                @keyup.enter.native="updateCodeTree('codeGroup', 1)"
                ></el-input>
            </td>
            <td v-else style="border-right:none">
              <span>{{codeDetail.codeGroup}}</span>
            </td>
            <td>
              <el-button
                v-if="codeGroupStatus && codeDetail.type != 'root'"
                type="primary" 
                size="small"
                @click="updateCodeTree('codeGroup')">등록</el-button>
            </td>
        </tr>
        <tr>
            <th colspan="2">
              코드타입
            </th>
            <td style="border-right:none">
              <span v-if="codeDetail.type === 'root'">-</span>
              <el-input 
                v-else
                v-model="codeDetail.codeType"
                ref="codeType"
                size="mini"
                @keyup.enter.native="updateCodeTree('codeType')"></el-input>
            </td>
            <td>
              <span v-if="codeDetail.type === 'root' && authKind"></span>
              <el-button
                v-else-if="codeTypeStatus && authKind"
                type="primary" 
                size="small"
                @click="updateCodeTree('codeType')">등록</el-button>
              <el-button
                v-else-if="authKind"
                type="primary" 
                size="small"
                @click="updateCodeTree('codeType')">수정</el-button>
            </td>
        </tr>
        <tr>
            <th colspan="2">
            코드
            </th>
            <td style="border-right:none">
              <ul class="selectable">
                <li class="selectable-list" 
                  v-for="(item, index) in codeDetail.childCodeList" 
                  :key="index" 
                  :style="{background: item.color, color: item.textColor}"
                  @click="setCodeChoice(item)">
                  <a>
                    <i v-if="item.uiType=='tree'" class="el-icon-folder"></i> 
                    <i v-else class="el-icon-circle-check"></i>
                     {{ item.codeName }}
                  </a>
                </li>
              </ul>
            </td>
            <td style="width:15%" >
            <div style="display:block " v-if="authKind">
                <el-button class="btn-manage" type="primary" icon="el-icon-caret-top" @click="codeSortChange(-1)"></el-button>
                <el-button class="btn-manage" type="primary" icon="el-icon-caret-bottom" @click="codeSortChange(1)"></el-button>
                <el-button class="btn-manage" type="info" icon="el-icon-delete" @click="deleteCode"></el-button>
            </div>
            </td>
        </tr>
        <tr v-if = "codeUiState">
          <th rowspan="4" style="width:10%">상세정보</th>
          <th style="width:120px">회사</th>
          <td colspan="2">{{ codeDetail.system }}</td>
        </tr>
        <tr v-if = "codeUiState">
          <th>코드명</th>
          <td colspan="2">
          <el-input v-if="isRegister" @keyup.enter.native="insertCodeValue" v-model="codeValueDetail.codeName" ref="codeName" size="mini" ></el-input>
          <el-input v-else @keyup.enter.native="updateCodeValue"  v-model="codeValueDetail.codeName" ref="codeName" size="mini"></el-input>
          </td>
        </tr>
        <tr v-if = "codeUiState">
          <th>값</th>
          <td colspan="2">
          <el-input v-if="isRegister" @keyup.enter.native="insertCodeValue" v-model="codeValueDetail.codeValue" ref="codeValue" size="mini"></el-input>
          <el-input v-else @keyup.enter.native="updateCodeValue"  v-model="codeValueDetail.codeValue" ref="codeValue" size="mini"></el-input>
          </td>
        </tr>
        <tr v-if = "codeUiState"> 
          <th>색상</th>
          <td colspan="2">
          <el-color-picker v-model="codeValueDetail.codeColor"></el-color-picker>
          <el-input v-model="codeValueDetail.codeColor" style="padding-left:10px;width:120px;top: -12px"></el-input>
          </td>
        </tr>
      </table>
    </div>
    <div class="aid-buttons" v-if = "codeUiState">
      <el-button type="primary" v-if = "isRegister && authKind" @click="insertCodeValue">코드 등록</el-button>
      <el-button type="success" v-if = "!isRegister && authKind"  @click="updateCodeValue">코드 수정</el-button>
      <el-button type="info" v-if = "authKind" @click="render">초기화</el-button>
    </div>
    </div>
</template>

<script>
import { setPatchParam } from '@/common/common'
import { updateCode, insertCode, getCodeSeqList, getSearchCode, getMainCode } from "@/api/poms-api/poms/code/code/code";


export default {
  name: "Code-mng-info"
  , props : {
    listLoading2: {
      type : Boolean
      , default : false
    }
    , systemCode: {
      type : Number
      , default : 0
    }
    , codeDetail : {
      type: Object
      , default : function() {
        return {
          codeSeq        : 0
          , selectedCode : ''
          , systemCode   : 0
          , system       : 'Lucy'
          , codeGroup    : 'Code Group'
          , codeValue    : ''
          , codeType     : 'Code Type'
          , codeName     : '채널'
          , value        : 'DC'
          , color        : '#FF1493'
          , uiType       : 'tree'
          , childUi      : 'tree'
          , childCodeList   : [{
            codeName     : '그룹코드1'
            , color      : 'default'
            , textColor  : ''
            , codeSort   : 0
            , codeSeq    : 0
            , clicked    : false
            , status     : false
            }
          ]
        }
      }
    }
  }

  , data() {
      return {
        codeType: undefined,
        codeGroup: undefined,
        selectedCondition: undefined,
        isRegister: true
        , codeGroupList: []
        , codeTypeList: []

        , codeGroupStatus : false
        , codeTypeStatus  : false 
        , codeValueDetail : {
          codeSeq     : 0
          , codeName  : ''
          , codeValue : ''
          , codeColor : ''
        }

        , codeUiState : false
      }
  }
  , methods: {

    /** Tree Info Update **/
    async updateCodeTree( searchField ){ 

      var param = {}

      /** codeGroup **/
      if(searchField === 'codeGroup'){
        if(this.codeDetail.codeGroup.trim() === ''){ 
          this.$alert('코드 그룹을 입력 하세요.')
          this.$refs.codeGroup.focus();
          return
        }

        let overLap = await this.codeGroupOverLap(this.codeDetail.codeGroup);
        if(!overLap){ 
          this.$alert('이미 존재하는 코드 그룹 입니다.')
          this.$refs.codeGroup.focus();
          return
        }
        
        await getCodeSeqList(this.codeDetail).then( response => {
          this.codeGroupList.push(this.codeDetail.codeSeq)
          this.setCodeSeqList('Group',response.data.response_data[0].childList);
        })

        param = setPatchParam('code_group', this.codeDetail.codeGroup, 'code_seq', this.codeGroupList.join(','));
        await updateCode(param).then( response => {
          this.makeAlert("Code Group", "코드 그룹 등록 완료되었습니다.")
          this.codeGroupStatus = false
          this.render();
        })
      }

      /** codeType **/
      if(searchField === 'codeType'){
        if(this.codeDetail.codeType.trim() === ''){ 
            this.$alert('코드 타입을 입력 하세요.')
            this.$refs.codeType.focus();
            return
        }

        await getCodeSeqList(this.codeDetail).then( response => {
          this.codeTypeList.push(this.codeDetail.codeSeq)
          this.setCodeSeqList('Type',response.data.response_data[0].childList);
        })

        param = setPatchParam('code_type', this.codeDetail.codeType, 'code_seq', this.codeTypeList.join(','));
        await updateCode(param).then( response => {
          var statueMsg = this.codeTypeStatus ? '등록' : '수정' 
          this.makeAlert("Code Type", "코드 타입 " + statueMsg + " 완료되었습니다.")
          this.codeTypeStatus = false
          this.render();
        })
      }
    }

    , async codeGroupOverLap ( codeGroup ){
      let param = {
          filedName : 'code_group'
          , value   : codeGroup
          , sysCode : this.systemCode
      }
      let result;
      await getMainCode(param).then( response => {
        result = (response.data.response_data.length > 0) ? false : true
      })
      return result
    }

    /** Value Info Insert **/
    , async insertCodeValue() {
      if(!this.verification()){ return }

      
      await insertCode(this.setParam()).then( response => {
        this.makeAlert("Code Value", "코드 값 등록 완료되었습니다.");
        this.resetCodeValueDetail()
        this.setChildCodeList()
        if( this.codeDetail.childCodeList.length === 0 ){
          this.$emit('parent:render')
        }
      })
    }


    /** Value Info Update **/
    , async updateCodeValue() {
      if(!this.verification()){ return }
      var param = {}
      param = setPatchParam('code_name', this.codeValueDetail.codeName, 'code_seq', this.codeValueDetail.codeSeq);
      await updateCode(param).then();

      param = setPatchParam('code_value', this.codeValueDetail.codeValue, 'code_seq', this.codeValueDetail.codeSeq);
      await updateCode(param).then();

      param = setPatchParam('code_color', this.codeValueDetail.codeColor, 'code_seq', this.codeValueDetail.codeSeq);
      await updateCode(param).then();

      this.makeAlert("Code Value", "코드 값 수정 완료되었습니다.");
      this.resetCodeValueDetail()
      this.setChildCodeList()
    }

    
    /** Value Info Delete **/
    , async deleteCode(){
      if(this.codeValueDetail.codeSeq === 0){
        this.$alert('선택된 코드가 없습니다.')
        return
      }
      var param = {}
      param = setPatchParam('code_use', 'N', 'code_seq', this.codeValueDetail.codeSeq);
      await updateCode(param).then( response => {
        this.makeAlert("Code Value", "코드 값 삭제 완료되었습니다.");
        this.resetCodeValueDetail()
        this.setChildCodeList()
      });
    }
    
    
    , async updateCode(param){
        await updateCode(param).then()
    }


    /** Value List Info Reset **/
    , async setChildCodeList(codeSeq){

      if(codeSeq === undefined){
        codeSeq = 0
      }

      if(this.codeDetail.type === 'root'){
        var param = {
          sysCode     : this.systemCode
          , filedName : 'code_p_seq'
          , value     : 0
          , depth     : 1
        }
        await getSearchCode(param).then( response => {
          this.setCodeDetailChildCodeFormat(response.data.response_data, codeSeq)
        })
      }else{
        await getCodeSeqList(this.codeDetail).then( response => {
          this.setCodeDetailChildCodeFormat(response.data.response_data[0].childList, codeSeq)
        })
      }
    }

    , setCodeDetailChildCodeFormat( childList, codeSeq ){

      var childCodeList = []
      this.codeDetail.childCodeList = []

      childList.forEach( x => {
        childCodeList.push({
          codeName     : x.code_name     || ''
          , codeColor  : x.code_color    || ''
          , codeValue  : x.code_value    || ''
          , codeSort   : x.code_sort_num || 0
          , codeSeq    : x.code_seq      || 0
          , codeSeq    : x.code_seq      || 0
          , uiType     : x.ui_type       || ''
          , status     : false
          , textColor  : (x.code_seq === codeSeq) ? 'white'   : ''
          , color      : (x.code_seq === codeSeq) ? '#497ac4' : ''
          , clicked    : (x.code_seq === codeSeq) ? true      : false
        })
      })

      childCodeList = _.orderBy(childCodeList , ['codeSort', 'ASC'])
      this.codeDetail.childCodeList = childCodeList
      this.resetCodes();
    }

    , setParam (){
      return {
        code_name       : this.codeValueDetail.codeName  || ''
        , code_color    : this.codeValueDetail.codeColor || ''
        , code_value    : this.codeValueDetail.codeValue || ''
        , code_p_seq    : this.codeDetail.codeSeq        || 0
        , code_p_value  : ''
        , code_type     : this.codeDetail.codeType  || ''
        , code_group    : this.codeDetail.codeGroup || ''
        , code_sort_num : ( _.max( _.map( this.codeDetail.childCodeList , 'codeSort')  ) + 1)  || 1
        , code_use      : 'Y'
        , child_use     : 'N'
        , ui_type       : 'val'
        , code_etc      : ''
        , code_depth    : 0
        , sys_code      : this.codeDetail.systemCode
      }
    }
    

    , verification (){
      var codeName = this.codeValueDetail.codeName.trim()
      if(codeName === ''){
        this.$alert('코드명 입력 하세요.')
        this.$refs.codeName.focus();
        return false
      }

      var codeValue = this.codeValueDetail.codeValue.trim()
      if(codeValue === ''){
        this.$alert('코드값 입력 하세요.')
          this.$refs.codeValue.focus();
        return false
      }
      return true
    }


    /** Code choice **/
    , setCodeChoice(item) {
      const list = this.codeDetail.childCodeList
      this.codeValueDetail.codeSeq   = item.codeSeq
      this.codeValueDetail.codeName  = item.codeName
      this.codeValueDetail.codeValue = item.codeValue
      this.codeValueDetail.codeColor = item.codeColor
      this.isRegister = false

      list.forEach(element => {
        element.textColor = ''
        element.clicked = false
        element.color = 'none'
      });

      item.textColor = 'white'
      item.clicked = true
      item.color = '#497ac4'
      this.resetCodes();
    }
      

    /** Code Reset **/
    , resetCodes (){

      var temp = this.codeDetail.childCodeList;
      this.codeDetail.childCodeList = [];

      temp = _.orderBy(temp , ['codeSort', 'ASC'])
      this.codeDetail.childCodeList = temp

      this.codeDetail.childCodeList.forEach( x => {
        if(this.codeValueDetail.codeSeq === x.codeSeq){
          x.color = '#497ac4'
          x.textColor = 'white'
          x.clicked = true
        }else{
          x.color = 'white'
          x.textColor = 'black'
          x.clicked = false
        }
      })
    }


    /** code Sort Change **/
    , async codeSortChange( value ){
      const list = this.codeDetail.childCodeList
      var index= list.findIndex(x => x.codeSeq === this.codeValueDetail.codeSeq);

      if( (index === list.length-1 && value === 1 ) || (index === 0 && value === -1 ) ){
        this.$alert('더 이상 이동할 수 없습니다')
        return
      }

      var targetOne = list[index]
      var targetTwo = list[index + value] 
      var param = {}

      param = setPatchParam('code_sort_num', targetTwo.codeSort, 'code_seq', targetOne.codeSeq);
      await updateCode(param).then();

      param = setPatchParam('code_sort_num', targetOne.codeSort, 'code_seq', targetTwo.codeSeq);

      await updateCode(param).then( response => {
        if(this.codeDetail.type === 'root'){
          this.setChildCodeList()
        }else{
          this.setChildCodeList(targetOne.codeSeq)
        }
      });
    }


    /** ChildList Code Seq Setting **/
    , setCodeSeqList(codeType, childList){
      childList.forEach( x => {
        if(codeType === 'Group'){
          this.codeGroupList.push(x.code_seq)
        }
        if(codeType === 'Type'){
          this.codeTypeList.push(x.code_seq)
        }
        if(x.childList.length > 0){
          this.setCodeSeqList(codeType, x.childList)
        }
      })  
    }


    , makeAlert(title, message){
      this.$notify({
        title      : title
        , message  : message
        , type     : "success"
        , duration : 2000
      });
    }


    , resetCodeValueDetail(){
      this.codeValueDetail =  {
          codeSeq     : 0
          , codeName  : ''
          , codeValue : ''
          , codeColor : ''
      }
      this.isRegister = true
    }
    

    , render(){
      this.resetCodeValueDetail()
      this.resetCodes ()
      this.$emit('parent:render')
    }
  }

  , watch : {
    codeDetail(val){
      this.codeGroupStatus = (val.codeGroup === '' && val.codeDepth == 0)   
      this.codeTypeStatus  = (val.codeType  === '' || val.type === 'root') 
      this.codeUiState = val.childUi === 'val' || ( val.childUi === 'tree' &&  val.childCodeList.length === 0 )
      this.resetCodeValueDetail()
      val.childCodeList = _.orderBy(val.childCodeList , ['codeSort', 'ASC'])
    }
  }

  , computed: {
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
