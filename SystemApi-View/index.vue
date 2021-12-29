<template>
    <div class = "app-container">
      <div class = "filter-container">
        <searchBox
          :option       = "option"
          @click:search = "searchClick"
        />
      </div>
  
      <div style="padding: 10px 10px 10px 0px;">
        <center-layout
            :btnInfo    = "btnInfo"
            @click:btn1 = "insertPopupState"
            @click:btn2 = "deleteSystemApi" />
    </div>
  
      <el-divider></el-divider>
      
      <el-table
        v-loading          = "listLoading"
        @selection-change  = "handleSelectionChange"
        @sort-change       = "sortChange"
        :data              = "systemApiList"
        :default-sort      = "{prop: 'puRegDate', order: 'descending'}"
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
          label     = "사용자명" 
          align     = "left" 
          sortable  = "true" 
          prop      = "system"
          width = "250" >
          <template slot-scope = "{row}">
            <span>{{ row.system }}</span>
          </template>
        </el-table-column>
        <el-table-column 
          label     = "담당자 1" 
          align     = "left" 
          prop      = "admin1"
          width = "200"> 
        </el-table-column>
        <el-table-column 
          label     = "담당자 2" 
          align     = "left" 
          prop      = "admin2"
          width = "200"> 
        </el-table-column>

        <el-table-column 
          label    = "도메인" 
          align    = "center" 
          sortable = "true" 
          prop     = "domain" > 
        </el-table-column>
        
        <el-table-column 
          label    = "자동분석 여부" 
          align    = "center" 
          sortable = "true" 
          prop     = "autoAnal"
          width    = "150">
          <template slot-scope="{row}">
            <span> {{ row.autoAnal === 'Y' ? 'ON' : 'OFF' }} </span>
          </template>
        </el-table-column>

        <el-table-column 
          label    = "등록일" 
          align    = "center" 
          sortable = "true" 
          prop     = "sysRegTimestamp"
          width    = "150">
        <template slot-scope="{row}">
          <span>{{ row.sysRegTimestamp | parseTime('{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
        
        <el-table-column 
          label = "수정" 
          align = "center"
          width = "100">
          <template slot-scope = "{row}" >
            <span @click = "updatePopupState(true, row)"> 
              <el-button size = "mini"> 
                <i class = "el-icon-s-tools" />
              </el-button>
            </span>
          </template>
        </el-table-column>
      </el-table>

      <div style="text-align: center;">
        <pagination 
          v-show      = "total>0" 
          :total      = "total" 
          :page.sync  = "param.rowNum" 
          :limit.sync = "param.limitNum" 
          @pagination = "fetchList" />
      </div>
      <div>
        <el-divider></el-divider>
      </div>
      <insertPopup
      :insertVisible              = "insertVisible"
      :key                        = "insertRenderingKey"
      @close:insertPopupState     = "insertPopupState"
      />
      
      <updatePopup
      :updateVisible              = "updateVisible"
      :systemKey                  = "updateSystemKey"
      :key                        = "updateRenderingKey"
      @close:updatePopupState     = "updatePopupState"
      />
  
    </div>
  </template>
  
  <script>
  import { getSysCodeList, getSysCodeTotalCnt, deleteApiSystem } from '@/api/poms-api/poms/system/company-mng/company-mng' 
  
  import _ from "lodash"
  import Pagination from '@/components/Pagination'
  import centerLayout from '@/views/common/layout/centerLayout1'


  import insertPopup from "./component/insert/insertPopup"
  import updatePopup from "./component/update/updatePopup"
  import searchBox from '@/views/common/search/search1'
  
  const searchOptionsUse = [
    { label: '전체', value: '' }
    , { label: 'ON', value: 'Y' }
    , { label: 'OFF', value: 'N' }
  ]
  
  const searchOptionsSearch = [
    { label: '시스템명'  , value: 'system' }
    , { label: '담당자 1', value: 'admin1' }
    , { label: '담당자 2', value: 'admin2' }
    , { label: '도메인'  , value: 'domain' }
  ]
  
  export default {
    name: 'System_Company_Mng'

    , components: { 
      Pagination
      , centerLayout
      , insertPopup
      , updatePopup
      , searchBox
    }
    , data() {
      return {
        systemApiList    : []
        , total            : 10
        , listLoading      : undefined  
  
        , param: { 
          systemKey        : 0
          , autoAnal       : ''
          , systemUse      : 'Y'
          , searchField    : ''
          , searchValue    : ''
          , rowNum         : 1
          , limitNum       : 10
          , orderByField   : 'system'
          , orderByValue   : 'ASC'
          , searchField    : 'systemKey'
          , searchValue    : '' 
        }
  
        , btnInfo : {
            btnName1 : '등록'
            , btnName2 : '삭제'
        }

        , insertVisible   : false
        , updateVisible   : false
        , updateSystemKey : 0
  
        , insertRenderingKey : 0
        , updateRenderingKey : -1
        , multipleSelection : []
        , option: {
          labelList     : searchOptionsSearch
          , searchField  : 'system'
          , searchValue  : ''
          , title1       : '자동분석 여부'
          , labelList1   : searchOptionsUse
          , searchValue1 : ''
        }
      }
    }
    , created() {
      this.fetchList()
    }
    , methods: {
  
      /**  User 목록 Setting **/
      async fetchList() {
        
        await getSysCodeList(this.param).then( response => {
            var list = []
            response.data.response_data.forEach( x => {
                list.push({
                    systemKey         : x.systemKey
                    , system          : x.system
                    , admin1          : x.admin1
                    , admin2          : x.admin2
                    , domain          : x.domain
                    , sysRegTimestamp : x.sysRegTimestamp
                    , autoAnal        : x.autoAnal
                })
            })
            this.systemApiList = list
        })
       
        await getSysCodeTotalCnt(this.param).then( response => {
            this.total = response.data.response_data.totalCnt
        })
      }

      , searchClick ( val ){
        this.param.searchField = val.searchField
        this.param.searchValue = val.searchValue
        this.param.autoAnal = val.searchValue1
        this.fetchList()
      }

      , sortChange ( row ) {
        this.param.orderByField = row.prop
        this.param.orderByValue =  row.order === 'descending' ? 'DESC' : 'ASC'
        this.fetchList()
      }

      , handleSelectionChange (val){
        this.multipleSelection = val;
      }

      , insertPopupState (status){
        this.insertVisible = status
      } 
      , updatePopupState (status, row){

        this.updateVisible = status

        if(status){
          this.updateSystemKey = row.systemKey
        }else{
          this.fetchList()
        }
      }

      , async deleteSystemApi (){

        let param = {
          searchField    : 'systemUse'
          , searchValue  : 'N'
          , seqListField : 'systemKey'
          , seqList      : _.map(this.multipleSelection, "systemKey").join(',')
        }

        await deleteApiSystem(param).then( response => {
          this.$notify({
                title      : '삭제'
                , message  : '삭제 완료 하였습니다.'
                , type     : 'success'
                , duration : 2000
          })
          this.fetchList();
        })
      }
    }
  
  
    , computed : { 
  
      authKind () {
        return this.$route.meta.authKind === 'A' ? true : false
      }
    }
  
    , filters: {
      
    } 
  }
  </script>
  
  <style scoped >
  .search-table { overflow-x: scroll; white-space:nowrap; font-family: "Malgun Gothic"; font-size: 11.5pt; background-color: #ebeef3; padding: 10px 20px 10px 20px; width: 100%; }
  .td1 { font-size: 13pt; font-weight: bold; width: 100px; padding-left: 1%; }
  .td2 { text-align: left; padding-top: 10px; }
  .td3 { text-align: left; padding-top: 10px; }
  .td4 { text-align: left; padding: 10px 0 0 30px;  }
  .td5 { text-align: left; text-align: right; padding: 10px 2% 0 60px ;}
  .td6 { text-align: right;}
  .select1{ width: 120px; }
  .input1{ width: 900px; }
  .btn1{ width: 100px;}
  </style>