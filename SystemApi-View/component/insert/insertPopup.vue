<template>
    <el-dialog
      :visible.sync = "insertVisible"
      :before-close = "popupClose"
      width         = "800px"
      top           = "2%"
      show-close
    >
      <div style="width:100%; height:710px;">
        <div style="width:50%;  float:left; height: 100%">
            <h2 style="padding: 0 0 20px 24px">고객사 정보 등록</h2>
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
      </div>
      <div slot = "footer" class = "dialog-footer">
        <el-button @click="systemApiSave()" type="primary">저장</el-button>
        <el-button @click="popupClose">취소</el-button>
      </div>
    </el-dialog>
  </template>
  
  <script>
  import { insertApiSystem } from '@/api/poms-api/poms/system/company-mng/company-mng' 
  import _ from 'lodash'
  
  export default {
    name : 'System_Company_Mng_Insert'

    , props: {
      insertVisible: {
          type : Boolean
          , default : false
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
      }
    }
  
  
    , methods: {
      
      async systemApiSave(){
        
        /** 유효성 검사 **/
        if( !this.verification( )){ return }

        await insertApiSystem(this.param).then( response => {

            this.$notify({
                title      : '등록'
                , message  : '등록 완료 하였습니다.'
                , type     : 'success'
                , duration : 2000
            })
            this.paramReset()
            this.$emit('close:insertPopupState', false)
        })
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
  
      /** POPUP CLOSE **/
      , popupClose() {
        this.paramReset()
        this.$emit('close:insertPopupState', false); 
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
      }
    }
    , watch : {
    }
  };
  </script>
  
  <style scoped lang="scss">
  ::v-deep .el-dialog__header {position: relative;height: 45px;padding: 0 !important;background: #1890ff;}::v-deep .el-dialog__title {position: absolute;height: auto !important;top: 50%;transform: translateY(-50%);height: 24px;padding: 0 0 0 20px;color: #fff;font-size: 16px;}::v-deep .el-dialog__headerbtn {position: absolute;top: 50%;right: 0;transform: translateY(-50%);padding: 0 20px 0 0;}::v-deep .el-dialog__close {color: #fff !important;}.div1 {color: black;}.div2 {min-height: 36px; background: #e5e9f2;height: 50px;max-height: 100%; ;display: block;border-radius: 10px;margin : 2px;padding : 8px 20px 2px 20px;} .div2-1div3 {min-height: 36px; background: #e5e9f2;height: 150px;max-height: 100%;overflow-y: auto !important;display: block;border-radius: 10px;margin : 2px;padding : 8px 5px 2px 20px;} .div3-1{width : 100%;}.input1 {width: 100px;margin: 0 10px 0 0;}.input2 {width: 450px;margin: 0 10px 0 0;}.input3 {width: 450px; margin: 0 10px 0 0;}.picker1 {width: 300px;}.span1{margin-right : 10px;} .span2{ margin : 0 10px 0 0 ; }
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
  