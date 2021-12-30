<template>
    <div v-if = "componentState">
        <div>
            <search-box
                :option         = "searchOption" 
                @excel:upload   = "uploadExcel" 
                @excel:download = "downloadExcelSample" 
                @click:search   = "searchClick" />
        </div>
        <div style="padding: 10px 10px 10px 0px;">
            <center-layout
                :btnInfo    = "btnInfo"
                @click:btn1 = "handleSelectionChange"
                @click:btn2 = "deletePatternAlert"
                @click:btn3 = "successPattern"/>
        </div>
        <div>
            <el-table
                ref                = "multipleTable"
                v-loading          = "listLoading"
                @selection-change  = "handleSelectionChange"
                @sort-change       = "sortChange"
                @refresh           = "tableRerender"
                :data              = "patternList"
                :key               = "tableRenderingKey"
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
                    label     = "요약" 
                    align     = "left" 
                    sortable  = "true" 
                    prop      = "apwlTitle">
                    <template 
                    slot-scope = "{row}"  >
                    <div 
                        style  = "cursor: pointer"  
                        @click = "patternTabState(row)" >
                        <span>{{ row.apwlTitle }}</span>
                    </div>
                    </template>
                </el-table-column>
                <el-table-column 
                    label     = "등록자" 
                    align     = "center" 
                    sortable  = "true" 
                    prop      = "puName" 
                    width     = "120">
                    <template 
                    slot-scope = "{row}"  >
                    <span>{{ row.puName }}</span>
                    </template>
                </el-table-column>
                <el-table-column 
                    label     = "등록일" 
                    align     = "center" 
                    sortable  = "true" 
                    prop      = "apwlDate" 
                    width     = "120">
                    <template 
                    slot-scope = "{row}"  >
                    <span>{{ row.apwlDate | parseTime("{y}-{m}-{d}") }}</span>
                    </template>
                </el-table-column>
                <el-table-column 
                    label     = "등록 수량" 
                    align     = "center" 
                    sortable  = "true" 
                    prop      = "apwlPatCount" 
                    width     = "110">
                    <template 
                    slot-scope = "{row}"  >
                    <div 
                        style  = "cursor: pointer"  
                        @click = "patternTabState(row)" >
                        <span>{{ row.apwlPatOkCount }} / {{ row.apwlPatCount }}</span>
                    </div>
                    </template>
                </el-table-column>
                <el-table-column 
                    label     = "처리 상태" 
                    align     = "center" 
                    sortable  = "true" 
                    prop      = "apwlPatState" 
                    width     = "120">
                    <template 
                    slot-scope = "{row}"  >
                    <el-tag :type = "row.apwlPatState | statusFilter">
                        <span>{{ row.apwlPatState === 1 ? '업로드 대기' : row.apwlPatState === 2 ? '업로드 완료' : row.apwlPatState === 3 ? '등록 완료' : '-'}}</span>
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
    </div>
</template>

<script>
import searchBox    from '@/views/common/search/search15'
import centerLayout from '@/views/common/layout/centerLayout2'
import Pagination   from '@/components/Pagination'
import { setPatchParam } from '@/common/common'
import { downloadByPatternUploadSample, uploadByPatternFile, getPatternWaitingList, getPatternWaitingListTotalCnt, getPomsUserData, updatePatternWaitingList } from "@/api/poms-api/dictionary/pattern/upload/upload"
import { blobDownload } from '@/utils/download'



export default {
    name : 'PattonDictinary_PattonUpload_Upload'
    , components : {
        searchBox
        , centerLayout
        , Pagination
    }

    , props : {
        componentState : {
            type : Boolean
            , default : false
        }
    }
    
    , data (){
        return {

            totalCnt : 0

            , param : {
                rowNum         : 0
                , limitNum     : 10
                , apwlPatState : 0
                , apwlSeq      : 0
                , useYn        : 'Y'
            }

            , patternList : []

            , searchOption : {
                title1 : '파일 양식'
                , buttonTitle : '다운로드'

                , title2 : '파일 선택'
                , searchValue2 : ''

                , title3 : '처리 상태'
                , searchValue3 : 0
                , labelList2 : [ 
                    { label: '전체'           , value: 0 } 
                    , { label: '업로드 대기중' , value: 1 } 
                    , { label: '업로드 완료'   , value: 2 } 
                ]
            }

            , btnInfo : {
                btnName1 : '전체 선택'
                , btnName2 : '삭제'
                , btnName3 : '등록 완료'
            }

            , tableRenderingKey : 1
            , listLoading       : false
            , multipleSelection : []
        }
    }

    , created () {
        this.fetchList()
    }

    , methods : {

        async getUserData (){

            for( let index in this.patternList ){
                let vo = this.patternList[index]
                await getPomsUserData(vo).then( response => {
                    vo.puName = response.data.response_data.puName
                })
            }
        }

        , async fetchList(){

            await getPatternWaitingList(this.param).then( response => {
                this.patternList = []
                response.data.response_data.forEach( x => {
                    console.log(x)
                    this.patternList.push({
                        apwlSeq          : x.apwlSeq
                        , apwlTitle      : x.apwlTitle
                        , puName         : ''
                        , puSeq          : x.puSeq
                        , apwlDate       : x.apwlDate
                        , apwlPatCount   : x.apwlPatCount
                        , apwlPatState   : x.apwlPatState
                        , apwlPatOkCount : x.apwlPatOkCount
                    })
                })
            })
 
            await getPatternWaitingListTotalCnt(this.param).then( response => {
                this.totalCnt = response.data.response_data.totalCnt
            })

            await this.getUserData()

        }

        /** 액셀 파일 업로드 **/
        , async uploadExcel( excelFile ){

            if(excelFile === undefined || excelFile === null){
                return
            }

            /**  type check 2021.12.21 **/
            var param = {
                file : excelFile.target.files[0]
            }

            this.$confirm('업로드 하시겠습니까 ?').then( _ => { 
                this.uploadByPatternFile(param)
            }).catch(_ => {
               return
            });
        }

        , async uploadByPatternFile ( param ){
            await uploadByPatternFile(param).then( response => {
                    this.$alert('액셀 파일이 업로드 되었습니다.')
                    this.fetchList()
                    excelFile.target.value = null;
                })
        }

        /** 액셀 샘플 파일 다운로드 **/
        , downloadExcelSample(  ){
            downloadByPatternUploadSample().then( response => {
                blobDownload(response)
            })
        }

        , searchClick (param){
            this.param.apwlPatState = param.state
            this.fetchList()
        }

        /** 전체 선택 **/
        , handleSelectionChange(val){
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
                this.deletePatternList()
            }).catch(_ => {
               return
            });
        }

        /** 삭제 **/
        , async deletePatternList (){
            let param = setPatchParam('useYn', 'N', 'apwlSeq',  _.map( this.multipleSelection , 'apwlSeq').join(',') );
            await updatePatternWaitingList(param).then( response => {
                this.fetchList();
            });
        }

        /** 처리 완료 **/
        , async successPattern(){
            let param = setPatchParam('apwlPatState', 3, 'apwlSeq',  _.map( this.multipleSelection , 'apwlSeq').join(',') );
            await updatePatternWaitingList(param).then( response => {
                this.fetchList();
            });
            
        }

        , sortChange () {

        }

        , tableRerender () {

        }

        , patternTabState(row){

            if(row.apwlPatState === 3){
                return
            }

            this.param.apwlSeq = row.apwlSeq
            this.$emit("reload:pattern", row)
        }
    }
    , filters: {
        statusFilter(status) {
        const statusMap = {
            1 : 'plain'
            , 2 : 'primary'
            , 3 : 'info'
        }
        return statusMap[status]
        }
    }
}
</script>