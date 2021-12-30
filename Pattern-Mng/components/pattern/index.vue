<template>
    <div v-if="!componentState">
        <div>
            <search-box
                :option          = "searchOption" 
                @reupload:excel  = "reuploadExcelupload" 
                @reload:upload   = "reloadUpload"
                @popup:category  = "updatePopupStateByCategory" 
                @update:category = "setPopupStateByCategory" 
                @click:search    = "searchClick" />
        </div>
        <div style="padding: 10px 10px 10px 0px;">
            <center-layout
                :btnInfo    = "btnInfo"
                @click:btn1 = "handleSelectionChange"
                @click:btn2 = "deletePatternAlert"
                @click:btn3 = "insertPattern"
                @click:btn4 = "updatePopupStateByCategory"
                @click:btn5 = "excelDownload"/>
        </div>
        <div>
            <el-table
                ref                = "multipleTable"
                v-loading          = "listLoading"
                @selection-change  = "handleSelectionChange"
                @sort-change       = "sortChange"
                @refresh           = "tableRerender"
                :data              = "patternList"
                :default-sort      = "{prop: 'puName', order: 'descending'}"
                :header-cell-style = "{textAlign: 'center'}"
                style              = "width: 100%;"
                highlight-current-row
                border
                fit
                >
                <el-table-column 
                    type  = "selection"
                    align = "center">
                </el-table-column>
                <el-table-column 
                    label     = "분류 정보" 
                    align     = "left" 
                    sortable  = "true" 
                    prop      = "icCode">
                    <template 
                    slot-scope = "{row}"  >
                    <span>{{ row.icCode }}</span>
                    </template>
                </el-table-column>
                <el-table-column 
                    label     = "패턴 타입" 
                    align     = "center" 
                    sortable  = "true" 
                    prop      = "patternType" 
                    width     = "110">
                    <template 
                    slot-scope = "{row}"  >
                    <span>{{ row.patternType }}</span>
                    </template>
                </el-table-column>
                <el-table-column 
                    label     = "패턴" 
                    align     = "left" 
                    sortable  = "true" 
                    prop      = "wordNm" 
                    width     = "200">
                    <template 
                    slot-scope = "{row}"  >
                    <span>{{ row.wordNm }}</span>
                    </template>
                </el-table-column>
                <el-table-column 
                    label     = "사용자 패턴식" 
                    align     = "left" 
                    sortable  = "true" 
                    prop      = "patternWordUser" 
                    width     = "250">
                    <template 
                    slot-scope = "{row}"  >
                    <div 
                        style  = "cursor: pointer"  
                        @click = "updatePopupState(true, row)" >
                        <span>{{ row.patternWordUser }}</span>
                    </div>
                    </template>
                </el-table-column>
                <el-table-column 
                    label     = "분석기 패턴식" 
                    align     = "left" 
                    sortable  = "true" 
                    prop      = "patternWord" 
                    width     = "250">
                    <template 
                    slot-scope = "{row}"  >
                    <div 
                        style  = "cursor: pointer"  
                        @click = "updatePopupState(true, row)" >
                        <span>{{ row.patternWord }}</span>
                    </div>
                    </template>
                </el-table-column>
                <el-table-column 
                    label     = "처리 상태" 
                    align     = "center" 
                    sortable  = "true" 
                    prop      = "apwdPatState" 
                    width     = "120">
                    <template 
                    slot-scope = "{row}"  >
                        <el-tag :type = "row.apwdPatState | statusFilter">
                            <span>{{ row.apwdPatState === 'Y' ? '완료' : '대기' }}</span>
                        </el-tag>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <div style="text-align: center;">
            <pagination 
              v-show      = "totalCnt > 0" 
              :total      = "totalCnt" 
              :page.sync  = "param.rowNum" 
              :limit.sync = "param.limitNum" 
              @pagination = "fetchList" />
        </div>
        <updatePopup
            :popupStatus  = "updatePopupStatus" 
            :categoryUpdateOption = "categoryUpdateOption"
            @close:popup  = "updatePopupState"
            /> 
        <setCategoryPopup
            :popupStatus      = "setPopupStatus" 
            @close:popup      = "updatePopupStateByCategory"
            @update:category  = "updateCategoryBranch"
            /> 
    </div>
</template>

<script>
import searchBox        from '@/views/common/search/search16'
import centerLayout     from '@/views/common/layout/centerLayout4'
import Pagination       from '@/components/Pagination'
import updatePopup      from './components/updateCategory/updatePopup'
import setCategoryPopup from './components/setCategory/setCategoryPopup'
import { setPatchParam } from '@/common/common'

import { getPatternWaitingData, getPatternWaitingDataTotalCnt, getCategoryName, getPatternWaitingDataByCategoryGroup, updatePatternWaitingData } from "@/api/poms-api/dictionary/pattern/upload/upload"

export default {
    name : 'PattonDictinary_PattonUpload_Patton'

    , components : {
        searchBox
        , centerLayout
        , Pagination
        , updatePopup
        , setCategoryPopup
    }

    , props : {
        patternInfo   : {
            type : Object
            , default : function(){
                return {
                    puName          : ''
                }
            }
        }
        , componentState : false
    }

    , data (){
        return {
            listLoading : false
            , totalCnt : 100

            , param : {
                apwlSeq        : 0
                , apwdIcState  : ''
                , apwdPatState : ''
                , useYn        : 'Y'
                , apwlSeq      : 0
                , rowNum       : 0
                , limitNum     : 10
            }

            , patternList : []
            , multipleSelection : []

            , searchOption : {
                title1 : '재 업로드'
                , buttonTitle1 : '작업 리스트'

                , title2 : '카테고리 일괄변경'
                , searchValue2 : ''
                , buttonTitle2 : '카테고리 변경'
                , labelList2 : []

                , title3 : '처리 상태'
                , searchValue3 : 1
                , labelList3 : [ 
                    { label: '전체'   , value: '' } 
                    , { label: '대기' , value: 'N' } 
                    , { label: '완료' , value: 'Y' } 
                ]
            }

            , btnInfo : {
                btnName1   : '전체 선택'
                , btnName2 : '삭제'
                , btnName3 : '패턴 등록'
                , btnName4 : '카테고리 변경'
                , btnName5 : '액셀 다운로드'
            }

            /** UPDATE CATEGORY POPUP DATA **/
            , setPopupStatus : false

            /** UPDATE PATTERN POPUP DATA **/
            , updatePopupStatus : false
            , categoryUpdateOption : {
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
                    { name: '분석명 1', type: 'info' } 
                    , { name: '분석명 2', type: 'info' } 
                ]
                , analysisResultValue : ''
                , analysisResultValueVasible : false

                , emotionList : [
                    { label : '긍정', value : 1 }
                    , { label : '부정', value : 2 }
                ] 
                , emotionValue : 2

                , patternTypeList : [ 
                    { label: '고유', value: 'G' } 
                    , { label: '인접', value: 'N' } 
                ]
                , patternTypeValue : 'N'
                , patternValue : '패턴이야야'

                , adjoinValue : 3
                
                , adjoinDirectionList : [
                    { label: '[F]순방향', value: 'F' },
                    { label: '[R]역방향', value: 'R' },
                    { label: '[A]양방향', value: 'A' },
                ]
                , adjoinDirectionValue : 'R'
                , patternQueryValue : '[N,F,2]([D]로켓,[D]배송)'

                , updateCategoryStatus  : ''
            }
        }
    }

    , methods : {

        updatePopupState (state, row){
            this.updatePopupStatus = state
        }

        , async fetchList (){
            
            this.patternList = []
            
            let param = {
                apwlSeq         : this.param.apwlSeq
                , apwdIcState   : ''
                , useYn         : this.param.useYn
                , orderByField  : this.param.orderByField
                , orderByValue  : this.param.orderByValue
                , rowNum        : this.param.rowNum
                , limitNum      : this.param.limitNum 
                
            }

            let patternList = [];
            await getPatternWaitingData(param).then ( response => {
                patternList = response.data.response_data;
            })
            
            for ( let index in patternList ){
                let x = patternList[index]

                if( x.apwdIcState === 'Y' ){
                    let param = {
                        systemKey       : x.systemKey
                        , icSeq         : x.icSeq
                    }

                    await getCategoryName(param).then( response => {
                        x.icCode = response.data.response_data.ac_name
                    })
                }
                
                this.patternList.push({
                    apwdSeq           : x.apwdSeq
                    , apwlSeq         : x.apwlSeq
                    , wordNm          : x.wordNm
                    , patternWord     : x.patternWord
                    , patternWordUser : x.patternWordUser
                    , patternType     : x.patternType
                    , icCode          : x.icCode
                    , icType          : x.icType
                    , patDate         : x.patDate
                    , apwdPatState    : x.apwdPatState
                    , weightedPoint   : x.weightedPoint
                })   
            }
            
            await getPatternWaitingDataTotalCnt(this.param).then ( response => {
                this.totalCnt = response.data.response_data.totalCnt
            })
            
            this.param.apwdIcState = 'N'

            await getPatternWaitingDataByCategoryGroup(this.param).then ( response => {
                this.searchOption.labelList2 = []
                response.data.response_data.forEach( (x, index) => {
                    this.searchOption.labelList2.push({
                        label   : x.icCode
                        , value : x.icCode
                    })
                })
            })
        }
        
        /** 작업 리스트 이동 **/
        , reloadUpload(){
            this.paramReset()
            this.$emit("reload:upload")
        }

        /** 재업로드 액셀 **/
        , reuploadExcelupload (val) {
            this.$alert('재 업로드 개발')
        }

        /** 처리 상태 **/
        , searchClick (val){
            this.param.apwdPatState = val.state
            this.fetchList()

        }

        /** 전체 선택 **/
        , handleSelectionChange (val){
            this.multipleSelection = []

            if(val != undefined){
                this.multipleSelection = val
            }else{
                this.patternList.forEach( x => {
                    this.multipleSelection.push(x)
                    this.$refs.multipleTable.toggleRowSelection(x);  
                })
            }

        }

        /** 삭제 확인 **/
        , deletePatternAlert (){
            if(this.multipleSelection.length === 0){
                this.$alert('선택된 데이터가 없습니다.')
                return
            }
            this.$confirm('삭제 하시겠습니까 ?').then( _ => { 
                this.deletePatternData()
            }).catch(_ => {
               return
            });
        }

        /** 삭제 **/
        , async deletePatternData (){
            let param = {}

            param = setPatchParam('useYn', 'N', 'apwdSeq',  _.map( this.multipleSelection , 'apwdSeq').join(',') );
            await updatePatternWaitingData(param).then( response => {
                this.fetchList();
            });
        }

        /** 패턴 등록 **/
        , insertPattern (){
            console.log('패턴 등록 ')

        }

        , updateCategoryBranch (val){

            if( this.updateCategoryStatus === 'Category Choice' ){
                this.updateCategoryChoice (val)
            }

            if( this.updateCategoryStatus === 'Category Group' ){
                this.updateCategoryGroup (val)
            }
        }


        /** 카테고리 선택 변경 **/
        , async updateCategoryChoice ( val ){
            await this.updateCategory( val , _.map( this.multipleSelection , 'apwdSeq').join(',') )
        }


        /** 카테고리 일괄 변경 **/
        , async updateCategoryGroup ( val ){ 
            
            let apwdSeqList = []
            let param = {
                apwlSeq       : this.param.apwlSeq
                , apwdIcState : 'N'
                , icCode      : this.searchOption.searchValue2
                , rowNum      : 0
                , limitNum    : 0
            }

            await getPatternWaitingDataTotalCnt(param).then( response => {
                param.limitNum = response.data.response_data.totalCnt
            })

            await getPatternWaitingData(param).then( response => {
                apwdSeqList = _.map( response.data.response_data , 'apwdSeq').join(',')
            })
 
            await this.updateCategory( val, apwdSeqList )
        }

        /** 카테고리 변경 **/
        , async updateCategory( updateVal, apwdSeqList ){
           let param = {}

            param = setPatchParam('systemKey', updateVal.systemKey, 'apwdSeq', apwdSeqList);
            await updatePatternWaitingData(param);

            param = setPatchParam('apwdIcState', 'Y', 'apwdSeq', apwdSeqList);
            await updatePatternWaitingData(param);
            
            param = setPatchParam('icSeq', updateVal.ic_seq, 'apwdSeq', apwdSeqList);
            await updatePatternWaitingData(param);
            
            param = setPatchParam('icCode', updateVal.ic_code, 'apwdSeq', apwdSeqList);
            await updatePatternWaitingData(param);

            param = setPatchParam('icType', updateVal.ic_type, 'apwdSeq', apwdSeqList);
            await updatePatternWaitingData(param);
            
            param = setPatchParam('weightedPoint', updateVal.weighted_point ,'apwdSeq', apwdSeqList);
            await updatePatternWaitingData(param);
            
            param = setPatchParam('apwdPatState', 'Y' ,'apwdSeq', apwdSeqList);
            await updatePatternWaitingData(param);
            
            _.remove( this.searchOption.labelList2 , function(category) {
                return updateVal === category;
            });

            this.searchOption.searchValue2 = ''
            this.fetchList ()
        }

        /** 카테고리 값 세팅  **/
        , setPopupStateByCategory (categorySeq){
            console.log('카테고리 값 세팅')
            console.log(categorySeq)  

        }

        /** 카테고리 일괄 변경 POPUP **/
        , updatePopupStateByCategory (way, state){
            
            if(way === 'Category Choice'){
                if(this.multipleSelection.length === 0){
                    this.$alert('선택된 카테고리가 없습니다.')
                    return
                }

                this.updateCategoryStatus = 'Category Choice'
            }


            if(way === 'Category Group'){
                if(state && this.searchOption.searchValue2 == ''){
                    alert('카테고리를 선택 하세요.')
                    return
                }

                this.updateCategoryStatus = 'Category Group'
            }
            
            this.setPopupStatus = state
        }

        /** 액셀 다운로드 **/
        , excelDownload (){
            this.$alert('액셀 다운로드 개발 예정 중')
        }

        /** 테이블 정렬 **/
        , sortChange () {

        }

        /** 테이블 렌더링 **/
        , tableRerender () {

        }
        
        , paramReset (){
            this.patternList = []
            this.param = {
                apwlSeq    : 0
                , rowNum   : 0
                , limitNum : 10
            }
            this.searchOption.labelList2 = []
            this.searchOption.searchValue2 = ''
        }
    }

    , watch : {
        patternInfo ( patternInfo ) {
            this.param.apwlSeq = patternInfo.apwlSeq
            this.fetchList()
        }
        , componentState (val){
            if(val){
                this.fetchList()
            }
        }
    }
    , filters: {
        statusFilter(status) {
        const statusMap = {
            'Y'   : 'info'
            , 'N' : 'plain'
        }
        return statusMap[status]
        }
    }
}
</script>