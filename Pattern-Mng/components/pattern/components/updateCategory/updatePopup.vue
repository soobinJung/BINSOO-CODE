<template>
    <el-dialog
      title         = "패턴 수정"
      :visible.sync = "popupStatus"
      width         = "1000px"
      top           = "2%"
      :before-close = "popupClose"
    show-close
    >
      <el-form>
        <!--  카테고리 -->
        <el-form-item label-width = "120px" label = "카테고리">
          <el-select 
            filterable 
            v-model     = "option.categoryValue1" 
            placeholder = "-" 
            @change     = "categoryChange(1, $event)">
            <el-option 
                v-for  = "item in option.categoryList1" 
                :label = "item.icName" 
                :key   = "item.icSeq" 
                :value = "item.icSeq"/>
          </el-select>

          <el-select 
            v-model     = "option.categoryValue2" 
            placeholder = "-" 
            @change     = "categoryChange(2, $event)">
            <el-option 
                label = "-" 
                value = 0 
                key   = "-"/>
            <el-option 
                v-for  = "item in option.categoryList2" 
                :label = "item.icName" 
                :key   = "item.icSeq" 
                :value = "item.icSeq"/>
          </el-select>
         
          <el-select 
            v-model     = "option.categoryValue3" 
            placeholder = "-">
            <el-option 
                label = "-" 
                value = 0 
                key   = "-"/>
            <el-option 
                v-for  = "item in option.categoryList3" 
                :label = "item.icName" 
                :key   = "item.icSeq" 
                :value = "item.icSeq"/>
          </el-select>
        </el-form-item>

        <!--  감성 -->
        <el-form-item label-width = "120px" label = "감성">
          <el-radio-group
            style     = "padding:10px 15px" 
            v-model   = "option.emotionValue">
            <el-radio 
                v-for  = "item in option.emotionList" 
                :label = "item.value"  
                :key   = "item.value">{{ item.label }}</el-radio>
          </el-radio-group>
        </el-form-item>

        <!--  패턴 방식 -->
        <el-form-item label-width = "120px" label = "패턴 방식">
          <el-radio-group 
            v-model = "option.patternTypeValue">
            <el-radio-button 
                v-for  = "item in option.patternTypeList" 
                :label = "item.value" 
                :key   = "item.value">{{ item.label }}</el-radio-button>
          </el-radio-group>
        </el-form-item>

        <!--  패턴 -->
        <el-form-item label-width = "120px" label = "패턴">
          <el-input 
            style               = "width:700px; padding:0 10px 0 0" 
            v-model             = "option.patternValue" 
            @keyup.enter.native = "patternAnalysis"/>
          <el-button 
            type   = "info" 
            @click = "patternAnalysis">분석</el-button>
        </el-form-item>

        <!--  분석 결과 -->
        <el-form-item label-width = "120px" label = "분석 결과">
          <el-tag
            closable
            v-for  = "tag in option.analysisResultList"
            :key   = "tag.name"
            @click = "handleChange(tag)"
            @close = "handleClose(tag)"
            :type  = "tag.type"
            :disable-transitions = "false"
            >
            {{tag.name}}
          </el-tag>
          <el-input
            class   = "input-new-tag"
            v-if    = "option.analysisResultValueVasible"
            v-model = "option.analysisResultValue"
            ref     = "saveTagInput"
            size    = "mini"
            @blur   = "handleInputConfirm"
            @keyup.enter.native = "handleInputConfirm"
          >
          </el-input>
          <el-button 
            v-else 
            class  = "button-new-tag" 
            size   = "small" 
            @click = "showInput">+ 추가</el-button>
          <div class = "info">
              <ul>
                  <li>
                        D ( 사전분석 ) 클릭 시 M ( 매칭분석 ) 으로 변경됩니다.
                  </li>
                  <li>
                        추가하여 생성된 M ( 매칭분석 ) 은 D ( 사전분석 ) 로 변경 불가합니다.
                  </li>
                  <li>
                        분석이 정확히 안되는 단어는 형태소 사전에 없는 단어입니다. 
                        매칭으로 등록하거나,
                        관리자에게 형태소 사전 추가를 요청해주세요.
                  </li>
              </ul>
          </div>
        </el-form-item>


        <el-form-item label-width = "120px" label = "인접 범위">
          <el-input-number 
            :max    = "20" 
            style   = "width:210px" 
            v-model = "option.adjoinValue"/>
          <div class = "info">
              <ul>
                  <li>
                      범위의 기준은 단어의 수량입니다.
                  </li>
                  <li>
                      인접 범위 자동계산 공식 ( 분석 단어수 - 1 ) * 2
                  </li>
              </ul>
          </div>
        </el-form-item>

        <el-form-item label-width = "120px" label = "인접 방향">
          <el-select 
            v-model     = "option.adjoinDirectionValue" 
            placeholder = "-">
            <el-option 
                v-for  = "item in option.adjoinDirectionList" 
                :label = "item.label" 
                :key   = "item.value" 
                :value = "item.value"/>
          </el-select>
        </el-form-item>

        <el-form-item label-width = "380px">
          <el-button 
            style  = "width:220px" 
            type   = "info" 
            @click = "generateQuery">쿼리생성</el-button>
        </el-form-item>

        <el-form-item label-width = "120px" label="패턴 쿼리">
          <el-input 
            disabled
            v-model = "option.patternQueryValue"/>
        </el-form-item>

      </el-form>
      <div slot = "footer" class = "dialog-footer">
        <el-button
          type      = "primary"
          :disabled = "option.patternTypeValue!='G' && option.patternQueryValue.length == 0" 
          @click    = "patternSave"
        >
            저장 후 계속
        </el-button>
        <el-button
          :disabled = "option.patternTypeValue!='G' && option.patternQueryValue.length == 0" 
          @click    = "patternSaveClose"
          type      = "primary"
        >
            저장후 닫기
        </el-button>
        <el-button @click = "popupClose">
            취소
        </el-button>
      </div>
        <div 
            v-if  = "option.patternTypeValue == 'G'" 
            class = "disable_dummy" 
            style = "height: calc(100% - 360px);">
        </div>
    </el-dialog>
  </template>
  
  <script>
  
  import {   } from "@/api/poms-api/dictionary/pattern/upload/upload"
  
  export default {
    name : 'PattonDictinary_PattonUpload_Patton_Update'

    , props:  {
      categoryUpdateOption : {
            categoryList1 : [
                { icName: 'Level 1 Category 1', icSeq: 1 } 
                , { icName: 'Level 1 Category 2', icSeq: 2 }
            ]
            , categoryValue1 : ''

            , categoryList2 : [
                { icName: 'Level 2 Category 1', icSeq: 1 } 
                , { icName: 'Level 2 Category 2', icSeq: 2 }
            ]
            , categoryValue2 : ''

            , categoryList3 : [
                { icName: 'Level 3 Category 1', icSeq: 1 } 
                , { icName: 'Level 3 Category 2', icSeq: 2 } 
            ]
            , categoryValue3 : ''
            
            ,  analysisResultList : [
                { name: '분석명 1', type: 1 } 
                , { name: '분석명 2', type: 2 } 
            ]

            , emotionList : [
                { label : '긍정', value : 1 }
                , { label : '부정', value : 2 }
            ] 
            , emotionValue : 2

            , patternTypeList : [ 
                { label: '고유', value: 'G' } 
                , { label: '인접', value: 'N' } 
            ]
            , patternTypeValue : 'G'

        }
        , popupStatus: {}
    }
    
    , data() {
      return {
        option : {
            categoryList1 : []
            , categoryValue1 : ''

            , categoryList2 : []
            , categoryValue2 : ''

            , categoryList3 : []
            , categoryValue3 : ''
            
            , analysisResultList : []
            , analysisResultValue : ''
            , analysisResultValueVasible : false

            , emotionList : [] 
            , emotionValue : 2

            , patternTypeList : []
            , patternTypeValue : ''
            , patternValue : ''

            , adjoinValue : 0
            
            , adjoinDirectionList : []
            , adjoinDirectionValue : ''
            , patternQueryValue : ''

        }
    }
    },
    methods: {
        categoryChange(level, event) {
        //     var param = {}
        //     param.ic_seq = event;
        //     param.chk_type = 1;
            
        //     for(var i=0; i<this.category1List.length; i++){
        //         if(this.category1List[i].ic_seq == event){
        //             if(this.category1List[i].ic_type == 1){
        //                 this.type = '1';
        //                 this.changeList2 = [];
        //                 this.changeValue2 = "0";
        //               this.changeList3 = [];
        //               this.changeValue3 = "0";
        //                 return;
        //             }else{
        //                 this.type = '2';
        //             }
        //         }
        //     }
            
        //     getChangeList(param).then((response) => {
        //       this.changeList2 = response.data.response_data;
        //       this.changeValue2 = "0";
        //       this.changeList3 = [];
        //       this.changeValue3 = "0";
        //       setTimeout(() => {
        //       }, 0.1 * 1000);
        //   });
        }
        
        , patternAnalysis (){

        }
        
        
        
        
        ,
        
    //     analysisWord(){
    //         if(this.option.searchValue3 == 'G'){
    //             return;
    //         }
    //       var word_nm = this.option.searchField4;
    //       var tempList = [];
    //       getMorphemeAnalysisWordBySentence(word_nm).then((response) => {
    //           this.option.labelList5 = [];
    //           tempList = response.data.response_data;
    //           var result = { name: '테스트', type: 'danger', class:'new'}
    //           var chk_cnt = 0;
              
    //           for(var i=0; i<tempList.length; i++){
    //               if(tempList[i].pat_type=='D'){
    //                   result = { name: tempList[i].word_nm, type: 'success'}
    //               }else{
    //                   result = { name: tempList[i].word_nm, type: 'danger'}
    //               }
    //               this.option.labelList5.push(result);
    //               chk_cnt++;
    //           }
              
    //           this.option.searchValue6 = (chk_cnt-1)*2;
    //           this.option.searchValue7 = 'F';
              
    //           setTimeout(() => {
    //           }, 0.1 * 1000);
    //       });
  
      
    //   },
        
        // (설정필요) 쿼리 생성
      generateQuery(){
        // var query = '[N,';
        // query += this.option.searchValue7 + ",";
        // query += this.option.searchValue6 + "](";
        
        // for(var i=0; i<this.option.labelList5.length; i++){
        //     query += "[";
        //     if(this.option.labelList5[i].type == 'success'){
        //         query += 'D';
        //     }else{
        //         query += 'F';
        //     }
        //     query += "]" + this.option.labelList5[i].name + ","; 
        // }
        // query = query.substring(0,query.length-1);
        
        // query += ")";
      
        // this.option.searchValue8 = query;
      
      },
      pattern() {
        //   if(this.functionFlag == 'i'){
        //       this.insertPattern();
        //   }
        //   if(this.functionFlag == 'u'){
        //       this.modifyPattern();
        //   }
      },
      
      patternSave() {
          
      },

      patternSaveClose() {
          
      },
      
        insertPattern() {
        //     var param = {}
        //     param.word_nm = this.option.searchField4;
        //     param.pattern_type = this.option.searchValue3; // 고유,인접
        //     if(this.option.searchValue3 == "N"){
        //         param.pattern_word = this.option.searchValue8;
        //     }
            
        //     if(this.category1List.length!=0){
        //         for(var i=0; i<this.category1List.length; i++){
        //             if(this.category1List[i].ic_seq == this.changeValue3){
        //                 param.ic_type = this.category1List[i].ic_type;
        //                 param.ic_code = this.category1List[i].ic_code;
        //                 param.systemKey = this.category1List[i].systemKey;
        //             }
        //         }
        //     }
            
        //     if(this.changeList2.length!=0){
        //         for(var i=0; i<this.changeList2.length; i++){
        //             if(this.changeList2[i].ic_seq == this.changeValue2){
        //                 param.ic_type = this.changeList2[i].ic_type;
        //                 param.ic_code = this.changeList2[i].ic_code;
        //                 param.systemKey = this.changeList2[i].systemKey;
        //             }
        //         }
        //     }
            
        //     if(this.changeList3.length!=0){
        //         for(var i=0; i<this.changeList3.length; i++){
        //             if(this.changeList3[i].ic_seq == this.changeValue3){
        //                 param.ic_type = this.changeList3[i].ic_type;
        //                 param.ic_code = this.changeList3[i].ic_code;
        //                 param.systemKey = this.changeList3[i].systemKey;
        //             }
        //         }
        //     }
            
        //     insertPattern(param).then((response) => {
        //       setTimeout(() => {
        //         this.$notify({
        //           title: 'Success',
        //           message: '패턴이 추가 되었습니다.',
        //           type: 'success',
        //           duration: 2000
        //         })
        //       }, 0.1 * 1000);
        //   });
        },
        
        modifyPattern() {
        //     var param = {}
        //     param.i_seq = this.selectPattern.i_seq;
        //     param.old_pat_seq = this.selectPattern.pat_seq;
        //     param.word_nm = this.option.searchField4;
        //     param.pattern_type = this.option.searchValue3; // 고유,인접
            
        //     param.old_ic_type = this.selectPattern.ic_type;
        //     param.old_ic_code = this.selectPattern.ic_code;
        //     param.old_systemKey = this.selectPattern.systemKey;
            
            
        //     if(this.option.searchValue3 == "N"){
        //         param.pattern_word = this.option.searchValue8;
        //     }
            
        //     if(this.category1List.length!=0){
        //         for(var i=0; i<this.category1List.length; i++){
        //             if(this.category1List[i].ic_seq == this.changeValue3){
        //                 param.ic_type = this.category1List[i].ic_type;
        //                 param.ic_code = this.category1List[i].ic_code;
        //                 param.systemKey = this.category1List[i].systemKey;
        //             }
        //         }
        //     }
            
        //     if(this.changeList2.length!=0){
        //         for(var i=0; i<this.changeList2.length; i++){
        //             if(this.changeList2[i].ic_seq == this.changeValue2){
        //                 param.ic_type = this.changeList2[i].ic_type;
        //                 param.ic_code = this.changeList2[i].ic_code;
        //                 param.systemKey = this.changeList2[i].systemKey;
        //             }
        //         }
        //     }
            
        //     if(this.changeList3.length!=0){
        //         for(var i=0; i<this.changeList3.length; i++){
        //             if(this.changeList3[i].ic_seq == this.changeValue3){
        //                 param.ic_type = this.changeList3[i].ic_type;
        //                 param.ic_code = this.changeList3[i].ic_code;
        //                 param.systemKey = this.changeList3[i].systemKey;
        //             }
        //         }
        //     }
            
        //     modifyPattern(param).then((response) => {
        //       setTimeout(() => {
        //         this.$notify({
        //           title: 'Success',
        //           message: '패턴이 수정 되었습니다.',
        //           type: 'success',
        //           duration: 2000
        //         })
        //       }, 0.1 * 1000);
        //   });
        },
        
        insertPatternClose() {
            // var param = {}
            // param.word_nm = this.option.searchField4;
            // param.pattern_type = this.option.searchValue3; // 고유,인접
            // if(this.option.searchValue3 == "N"){
            //     param.pattern_word = this.option.searchValue8;
            // }
            
            // if(this.category1List.length!=0){
            //     for(var i=0; i<this.category1List.length; i++){
            //         if(this.category1List[i].ic_seq == this.changeValue3){
            //             param.ic_type = this.category1List[i].ic_type;
            //             param.ic_code = this.category1List[i].ic_code;
            //             param.systemKey = this.category1List[i].systemKey;
            //         }
            //     }
            // }
            
            // if(this.changeList2.length!=0){
            //     for(var i=0; i<this.changeList2.length; i++){
            //         if(this.changeList2[i].ic_seq == this.changeValue2){
            //             param.ic_type = this.changeList2[i].ic_type;
            //             param.ic_code = this.changeList2[i].ic_code;
            //             param.systemKey = this.changeList2[i].systemKey;
            //         }
            //     }
            // }
            
            // if(this.changeList3.length!=0){
            //     for(var i=0; i<this.changeList3.length; i++){
            //         if(this.changeList3[i].ic_seq == this.changeValue3){
            //             param.ic_type = this.changeList3[i].ic_type;
            //             param.ic_code = this.changeList3[i].ic_code;
            //             param.systemKey = this.changeList3[i].systemKey;
            //         }
            //     }
            // }
            
            // insertPattern(param).then((response) => {
            //   setTimeout(() => {
            //     this.$notify({
            //       title: 'Success',
            //       message: '패턴이 추가 되었습니다.',
            //       type: 'success',
            //       duration: 2000
            //     })
            //     this.local_dialogCreateFormVisible = "update"
            //   }, 0.1 * 1000);
        //   });
        },
        
        modifyPatternClose() {
        //     var param = {}
        //     param.i_seq = this.selectPattern.i_seq;
        //     param.pat_seq = this.selectPattern.pat_seq;
        //     param.word_nm = this.option.searchField4;
        //     param.pattern_type = this.option.searchValue3; // 고유,인접
            
        //     if(this.option.searchValue3 == "N"){
        //         param.pattern_word = this.option.searchValue8;
        //     }
            
        //     if(this.category1List.length!=0){
        //         for(var i=0; i<this.category1List.length; i++){
        //             if(this.category1List[i].ic_seq == this.changeValue3){
        //                 param.ic_type = this.category1List[i].ic_type;
        //                 param.ic_code = this.category1List[i].ic_code;
        //                 param.systemKey = this.category1List[i].systemKey;
        //             }
        //         }
        //     }
            
        //     if(this.changeList2.length!=0){
        //         for(var i=0; i<this.changeList2.length; i++){
        //             if(this.changeList2[i].ic_seq == this.changeValue2){
        //                 param.ic_type = this.changeList2[i].ic_type;
        //                 param.ic_code = this.changeList2[i].ic_code;
        //                 param.systemKey = this.changeList2[i].systemKey;
        //             }
        //         }
        //     }
            
        //     if(this.changeList3.length!=0){
        //         for(var i=0; i<this.changeList3.length; i++){
        //             if(this.changeList3[i].ic_seq == this.changeValue3){
        //                 param.ic_type = this.changeList3[i].ic_type;
        //                 param.ic_code = this.changeList3[i].ic_code;
        //                 param.systemKey = this.changeList3[i].systemKey;
        //             }
        //         }
        //     }
        //     modifyPattern(param).then((response) => {
        //       setTimeout(() => {
        //         this.$notify({
        //           title: 'Success',
        //           message: '패턴이 수정 되었습니다.',
        //           type: 'success',
        //           duration: 2000
        //         })
        //         this.local_dialogCreateFormVisible = "update"
        //       }, 0.1 * 1000);
        //   });
        },
        
      /** popup 닫기 **/
      popupClose() {
        this.$emit("close:popup", false)
      }

      /** 태그 삭제 **/
      , handleClose(tag){
        this.option.analysisResultList.splice(
            this.option.analysisResultList.indexOf(tag)
            , 1
        );
      }


      /** 태그 추가 창 보이기 **/
      , showInput() {
          this.option.analysisResultValueVasible = true;
          this.$nextTick(_ => {
            this.$refs.saveTagInput.$refs.input.focus();
          });
        }


      /** 태그 추가 **/
      , handleInputConfirm() {
        let inputValue = this.option.analysisResultValue;
        if (inputValue) {
          const result = 
          this.option.analysisResultList.push(
            { 
                name    : inputValue
                , type  : 'danger'
                , class :'new'}
          );
        }
        this.option.analysisResultValueVasible = false;
        this.option
        
        .analysisResultValue = '';
      }

      /**  **/
      , handleChange(tag){
        if(tag.class){
          return
        }
        if(tag.type == 'danger'){
          tag.type = 'success'
        }else{
          tag.type = 'danger'
        }
      }

      , setOption(option){
          this.option.categoryList1  = option.categoryList1 || []
          this.option.categoryValue1 = option.categoryValue1 || 0

          this.option.categoryList2  = option.categoryList2  || []
          this.option.categoryValue2 = option.categoryValue2 || 0

          this.option.categoryList3  = option.categoryList3  || []
          this.option.categoryValue3 = option.categoryValue3 || 0
            
          this.option.analysisResultList  = option.analysisResultList  || []
          this.option.analysisResultValue = option.analysisResultValue || 0
          this.option.analysisResultValueVasible = option.analysisResultValueVasible || false

          this.option.emotionList  = option.emotionList   || []
          this.option.emotionValue = option.emotionValue  || 0

          this.option.patternTypeList  = option.patternTypeList  || []
          this.option.patternTypeValue = option.patternTypeValue || 0
          this.option.patternValue     = option.patternValue     || 0

          this.option.adjoinValue = option.adjoinValue || 0
            
          this.option.adjoinDirectionList  = option.adjoinDirectionList  || []
          this.option.adjoinDirectionValue = option.adjoinDirectionValue || 0
          this.option.patternQueryValue    = option.patternQueryValue    || ''
      }
    }
    
    , watch : {
    
        popupStatus ( value ){

          /** 부모에게 전달 받은 데이터 Local 데이터로 세팅 **/
          this.setOption(this.categoryUpdateOption)
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
  /* dialog 헤더 */
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
  
  /* dialog title */
  .dialog-title {
    color: black;
  }
  
  /* dialog안에 grid */
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
  
  /* dialog2번 안에 infos */
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
    .disable_dummy {
      position: absolute;
      bottom: 60px;
      left: 0;
      width: 100%;
      height: calc(100% - 202px);
      background: rgba(255, 255, 255, 0.85);
      z-index: 1;
      -webkit-transition-property: all;
      -webkit-transition-duration: 0.18s;
      -webkit-transition-delay: 0s;
      -webkit-transition-timing-function: cubic-bezier(0.5, 0, 0.5, 1);
      transition-property: all;
      transition-duration: 0.18s;
      transition-delay: 0s;
      transition-timing-function: cubic-bezier(0.5, 0, 0.5, 1);
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
  </style>
  