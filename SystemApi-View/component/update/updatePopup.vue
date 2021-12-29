<template>
    <el-dialog
      :visible.sync = "updateVisible"
      :before-close = "popupClose"
      width         = "1700px"
      top           = "1%"
      show-close
    >
      <div style="width:100%; height:680px;">
        <div style="width:50%;  float:left; height: 100%">
            <h2 style="padding: 0 0 20px 24px">고객사 정보 수정</h2>
            <el-form
            label-width    = "150px"
            >
                <el-form-item label = "* 고객사명">
                    <el-col>
                        <div style = "height: 50px; padding : 8px 0 8px 40px; margin: 0 100px 0 0; border-radius: 10px;">
                            <el-input
                            v-model = "param.client"
                            ref     = "client" 
                            size    = "small"
                            width   = "1000px"
                            type    = "text"
                            class   = "input2"
                            />
                        </div>
                    </el-col>
                </el-form-item>
                <el-form-item label = "* 시스템명">
                    <el-col>
                        <div style = "height: 50px; padding : 8px 0 8px 40px; margin: 0 100px 0 0; border-radius: 10px;">
                            <el-input
                            v-model = "param.system"
                            ref     = "system" 
                            size    = "small"
                            width   = "3000px"
                            type    = "text"
                            class   = "input2"
                            />
                        </div>
                    </el-col>
                </el-form-item>
                <el-form-item label = "* 관리자 (정)">
                    <el-col>
                        <div style = "height: 50px; padding : 8px 0 8px 40px; margin: 0 100px 0 0; border-radius: 10px;">
                            <el-input
                            v-model = "param.admin1"
                            ref     = "admin1" 
                            size    = "small"
                            width   = "3000px"
                            type    = "text"
                            class   = "input2"
                            />
                        </div>
                    </el-col>
                </el-form-item>

                <el-form-item label = "관리자 (부)">
                    <el-col>
                        <div style = "height: 50px; padding : 8px 0 8px 40px; margin: 0 100px 0 0; border-radius: 10px;">
                            <el-input
                            v-model = "param.admin2"
                            ref     = "admin2" 
                            size    = "small"
                            width   = "3000px"
                            type    = "text"
                            class   = "input2"
                            />
                        </div>
                    </el-col>
                </el-form-item>

                <el-form-item label = "* 호스트">
                    <el-col>
                        <div style = "height: 50px; padding : 8px 0 8px 40px; margin: 0 100px 0 0; border-radius: 10px;">
                            <el-input
                            v-model = "param.host"
                            ref     = "host" 
                            size    = "small"
                            width   = "3000px"
                            type    = "text"
                            class   = "input2"
                            />
                        </div>
                    </el-col>
                </el-form-item>

                <el-form-item label = "도메인">
                    <el-col>
                        <div style = "height: 50px; padding : 8px 0 8px 40px; margin: 0 100px 0 0; border-radius: 10px;">
                            <el-input
                            v-model = "param.domain"
                            ref     = "domain" 
                            size    = "small"
                            width   = "3000px"
                            type    = "text"
                            class   = "input2"
                            />
                        </div>
                    </el-col>
                </el-form-item>

                <el-form-item label = "자동분석 여부">
                    <el-col>
                        <div style = "height: 50px; padding : 0px 0 8px 40px; margin: 0 100px 0 0; border-radius: 10px;">
                            <el-switch v-model = "param.autoAnal"></el-switch>
                        </div>
                    </el-col>
                </el-form-item>

                <el-form-item label = "사용 여부">
                    <el-col>
                        <div style = "height: 50px; padding : 0px 0 8px 40px; margin: 0 100px 0 0; border-radius: 10px;">
                            <el-switch v-model = "param.systemUse"></el-switch>
                        </div>
                    </el-col>
                </el-form-item>

                <el-form-item label = "메모">
                    <el-col>
                        <div style = "height: 180px; padding : 0 0 5px 40px; margin: 0 100px 0 0; border-radius: 10px;">
                            <el-input
                            v-model = "param.description"
                            ref     = "description" 
                            size    = "small"
                            type    = "textarea"
                            :rows   = "3"
                            style   = "width: 450px; margin: 0 10px 0 0;  line-height: 20px; font-size: 15px"
                            />
                        </div>
                    </el-col>
                </el-form-item>
            </el-form>
        </div>

        <div style="width:48%; float:left; height: 100%">
          <div style="width:100%">
            <h2> 사전 카테고리 선택 </h2>
          </div>
          <div style = "width:100%; height:300px; border : 3px solid #e3e3e3; padding : 2% 2% 2% 2%" > 
            <el-form
              label-width    = "150px"
            > 
              <el-form-item :label = "vo.title" v-for = "vo in dictionaryCategorys"> 
                <el-col>
                  <div style = "float: left; width: 30px; height: 50px; padding : 0px 0 8px 40px; margin: 0 100px 0 0;">
                    <el-switch @change = changeDictionaryStatus(vo) v-model = "vo.variable"></el-switch>
                  </div>
                  <div style = "float: left; width: 30px; height: 50px; padding : 0px 0 8px 40px; margin: 0 100px 0 0;">
                    <el-button 
                      v-if = "vo.variable"
                      @click = "dictionaryStatus( true, vo )" > 사전 보기 </el-button>
                  </div>
                  <div style = "float: left; width: 250px; height: 50px; padding : 0px 0 8px 20px;">
                    <span v-if = "vo.variable">  {{vo.icFullName}} </span>
                  </div>
                </el-col>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </div>
      <div slot = "footer" class = "dialog-footer">
        <el-button @click="systemApiSave()" type="primary">저장</el-button>
        <el-button @click="popupClose">취소</el-button>
      </div>
      <dictionaryPopup
      :popupStatus = "dictionaryPopupVal"
      :info        = "dictionaryInto"
      @close:popup = "dictionaryStatus"
      @update:diccategory = "updateDictionaryCategory"
      >
      </dictionaryPopup>
    </el-dialog>
  </template>
  
<script>
import { updateApiSystem , getApiSystemFindId , selectPatternInfoByApiSystem, updatePatternInfoByApiSystem, insertPatternInfoByApiSystem, getMappingNameByApiSystemAndCategory} from '@/api/poms-api/poms/system/company-mng/company-mng' 
import _ from 'lodash'
import { setPatchParam } from '@/common/common'
import dictionaryPopup from './dictionaryPopup.vue'

export default {
  name : 'System_Company_Mng_Update'
  
  , components  : {
    dictionaryPopup
  }

  , props: {
      updateVisible: {
          type : Boolean
          , default : false
      }
      , systemKey: {
          type : Number
          , default : 0
      }
    }
  
  , data() {
    return {
      param : {
        systemKey     : ''
        , system      : ''
        , admin1      : ''
        , admin2      : ''
        , domain      : '' 
        , host        : ''
        , host2       : ''
        , description : ''
        , autoAnal    : true
        , issueData   : ''
        , client      : ''
        , systemUse   : true
        , analysisList : {
          related     : true
          , data      : true
          , emotional : true
          , spam      : true
        }
      }
      , dictionaryPopupVal : false
      , dictionaryInto : {
        systemKey : 0
        , icType  : 0
      }

      , dictionaryCategorys : [
        { systemPatternSeq : 0, ic_seq : 0, icType : 1    , icFullName: '', title : '감성 분석',   variable : false, insertYn : false }
        , { systemPatternSeq : 0, ic_seq : 0, icType : 3  , icFullName: '', title : '연관어 분석', variable : false, insertYn : false }
        , { systemPatternSeq : 0, ic_seq : 0, icType : 5  , icFullName: '', title : '데이터 분류', variable : false, insertYn : false }
        , { systemPatternSeq : 0, ic_seq : 0, icType : 99 , icFullName: '', title : '스팸 분석',   variable : false, insertYn : false }
      ]
    }
  }
  
  
  , methods: {
      
    async fetchData (){ 
        await getApiSystemFindId({systemKey : this.systemKey}).then( response =>{
          let data = response.data.response_data[0]

          this.param.systemKey   = data.systemKey
          this.param.system      = data.system
          this.param.client      = data.client
          this.param.admin1      = data.admin1
          this.param.admin2      = data.admin2
          this.param.domain      = data.domain
          this.param.host        = data.host
          this.param.description = data.description
          this.param.autoAnal    = data.autoAnal  === 'Y' ? true : false
          this.param.systemUse   = data.systemUse === 'Y' ? true : false
        })
    }

    , async selectPatternInfoByApiSystem (){

      let dictionaryCategorys = this.dictionaryCategorys;
      let list = []

      /** API SYSTEM / 허용 CATEGORY 세팅 **/
      await selectPatternInfoByApiSystem({systemKey : this.systemKey}).then( response => {
        list = response.data.response_data; 
      })

      for( let index in dictionaryCategorys ){

        let x = dictionaryCategorys[index]
        let y = _.find(list , { icType : x.icType })

        if( y != undefined ){
          x.icSeq = y.icSeq;
          x.variable = ( y.useYn === 'N' ) ? false : true 
          x.insertYn = true
          x.systemPatternSeq = y.systemPatternSeq;

          /** 허용 CATEGORY 상위 ~ 자신 이름 세팅 **/
          if(Number(x.icSeq) != 0){
            await getMappingNameByApiSystemAndCategory({
              systemKey : this.systemKey
              , icSeq   : x.icSeq
            }).then( response => {
              x.icFullName = response.data.response_data.ac_name
            })
          }
        }
      }

      this.dictionaryCategorys = dictionaryCategorys
    }

    , async updateDictionaryCategory(vo){
      let systemPatternSeq = _.find(this.dictionaryCategorys, { icType : vo.ic_type }).systemPatternSeq
      let param = setPatchParam('icSeq', vo.ic_seq , 'systemPatternSeq', systemPatternSeq )
      await this.updatePatternInfoByApiSystem(param).then( response => {
        this.selectPatternInfoByApiSystem()
      })
    }

    , async systemApiSave(){
      if( !this.verification( )){ return }

      await this.updateApiSystem().then( response => {
          this.$notify({
              title      : '수정'
              , message  : '수정 완료 하였습니다.'
              , type     : 'success'
              , duration : 2000
          })
          this.paramReset()
          this.$emit('close:updatePopupState', false)
      })
    }

    , async updateApiSystem(){

      let param = {}
      param = setPatchParam('client', this.param.client, 'systemKey', this.systemKey);
      await updateApiSystem(param);

      param = setPatchParam('system', this.param.system, 'systemKey', this.systemKey);
      await updateApiSystem(param);

      param = setPatchParam('admin1', this.param.admin1, 'systemKey', this.systemKey);
      await updateApiSystem(param);

      param = setPatchParam('admin1', this.param.admin1, 'systemKey', this.systemKey);
      await updateApiSystem(param);

      param = setPatchParam('admin2', this.param.admin2, 'systemKey', this.systemKey);
      await updateApiSystem(param);

      param = setPatchParam('host', this.param.host, 'systemKey', this.systemKey);
      await updateApiSystem(param);

      param = setPatchParam('domain', this.param.domain, 'systemKey', this.systemKey);
      await updateApiSystem(param);

      param = setPatchParam('autoAnal', this.param.autoAnal, 'systemKey', this.systemKey);
      await updateApiSystem(param);

      param = setPatchParam('systemUse', this.param.systemUse, 'systemKey', this.systemKey);
      await updateApiSystem(param);

      param = setPatchParam('description', this.param.description, 'systemKey', this.systemKey);
      await updateApiSystem(param);
    }

    /** VALUES 검증 **/
    , verification( ){
      if(this.param.client == null || this.param.client == '' ){
        this.$alert('고객사명을 입력해주세요.')
        this.$refs.client.focus();
        return false;
      }

      if(this.param.system == null || this.param.system == '' ){
        this.$alert('시스템명을 입력해주세요.')
        this.$refs.system.focus();
        return false;
      }


      if( this.param.admin1 == null || this.param.admin1 == '' ){
          this.$alert('관리자를 입력해주세요.')
          this.$refs.admin1.focus();
        return false;
      }

      if( this.param.host == null || this.param.host== '' ){
          this.$alert('호스트를 입력해주세요.')
          this.$refs.host.focus();
          return false;
      }

      this.param.autoAnal    = this.param.autoAnal  === true ?  'Y' : 'N' 
      this.param.systemUse   = this.param.systemUse === true ?  'Y' : 'N' 

      return true;
    }

    , popupClose() {
      this.paramReset()
      this.$emit('close:updatePopupState', false); 
    }
  
    , dictionaryStatus (value, info){
      if(value){
        let param = {
          systemKey : this.param.systemKey
          , icType  : info.icType
        }
        this.dictionaryInto = param
      }
      this.dictionaryPopupVal = value
    }

    , changeDictionaryStatus ( vo ){
      if(vo.insertYn){
        let param = setPatchParam('useYn', vo.variable ? 'Y' : 'N' , 'systemPatternSeq', vo.systemPatternSeq)
        this.updatePatternInfoByApiSystem(param)
      }

      if(!vo.insertYn){
        vo.systemKey = this.systemKey
        vo.icType    = vo.icType
        vo.icSeq     = vo.systemPatternSeq
        vo.useYn     = 'Y'
        this.insertPatternInfoByApiSystem(vo)
      }
    }

    , async insertPatternInfoByApiSystem(vo){
      await insertPatternInfoByApiSystem(vo).then( response => {
        vo.insertYn = true
      })
    }
    
    , async updatePatternInfoByApiSystem(param){
      await updatePatternInfoByApiSystem(param).then( response => {
        
      })
    }

    , paramReset(){
      this.param =   {
        systemKey     : ''
        , system      : ''
        , admin1      : ''
        , admin2      : ''
        , domain      : '' 
        , host        : ''
        , host2       : ''
        , description : ''
        , autoAnal    : true
        , issueData   : ''
        , client      : ''
        , systemUse   : true
        , analysisList : {
          related     : true
          , data      : true
          , emotional : true
          , spam      : true
        }
      }

      this.dictionaryCategorys =[
        { systemPatternSeq : 0, icType : 1    , title : '감성 분석',   variable : false, insertYn : false }
        , { systemPatternSeq : 0, icType : 3  , title : '연관어 분석', variable : false, insertYn : false }
        , { systemPatternSeq : 0, icType : 5  , title : '데이터 분류', variable : false, insertYn : false }
        , { systemPatternSeq : 0, icType : 99 , title : '스팸 분석',   variable : false, insertYn : false }
      ]
    }
  }

  , watch : {
    updateVisible( val ){
      if(val){
        this.fetchData();
          this.selectPatternInfoByApiSystem();
      }
    }
  }
};
</script>
  
  <style scoped lang="scss">
  ::v-deep .el-dialog__header {
    position: relative;
    height: 45px;
    padding: 0 !important;background: #1890ff;
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
    top: 50%;right: 0;
    transform: translateY(-50%);
    padding: 0 20px 0 0;
  }
  
  ::v-deep .el-dialog__close {
    color: #fff !important;
  }
  
  .div1 {
    color: black;
  }
  
  .div2 {
    min-height: 36px; 
    background: #e5e9f2;
    height: 50px;
    max-height: 100%;;
    display: block;
    border-radius: 10px;
    margin : 2px;
    padding : 8px 20px 2px 20px;
  }
  
  .div2-1div3 {
    min-height: 36px; 
    background: #e5e9f2;
    height: 150px;
    max-height: 100%;
    overflow-y: auto !important;
    display: block;
    border-radius: 10px;
    margin : 2px;
    padding : 8px 5px 2px 20px;
  } 
  
  .div3-1{
    width : 100%;
  }
  
  .input1 {
    width: 100px;
    margin: 0 10px 0 0;
  }
  
  .input2 {
    width: 450px;
    margin: 0 10px 0 0;
  }
  
  .input3 {
    width: 450px; 
    margin: 0 10px 0 0;
  }
  
  .picker1 {
    width: 300px;
  }
  
  .span1{
    margin-right : 10px;
  }
  
  .span2{ 
    margin : 0 10px 0 0 ; 
  }
  
  .div-background{
    background :#f7f7f7; 
    font-weight: normal;
  }
  
  .div-background:hover {
    background-color: #d9d9d9;
    font-weight : bold;
    border : 1px solid black;
  }
   
  </style>
  