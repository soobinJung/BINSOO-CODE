package com.rsn.POMS.api.dictionary.advanced.categorywaiting.vo;

import java.util.List;
import com.rsn.POMS.api.setting.common.vo.SearchVo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryWaitingVO  extends SearchVo {

	private int APWL_SEQ = 0;
	
	@ApiModelProperty(
			value      = "대기 패턴 목록 일련번호"
			, name     = "apwlSeq"
			, dataType = "int"
			, example  = "0")
	private int apwlSeq = 0;
	
	@ApiModelProperty(
			value      = "카테고리 일련번호"
			, name     = "icSeq"
			, dataType = "int"
			, example  = "0")
	private int icSeq = 0;
	
	
	@ApiModelProperty(
			value      = "대기 패턴 목록명"
			, name     = "apwlTitle"
			, dataType = "String"
			, example  = "")
	private String apwlTitle = "";
	
	
	@ApiModelProperty(
			value      = "대기 패턴 목록 등록일"
			, name     = "apwlDate"
			, dataType = "String"
			, example  = "")
	private String apwlDate = "";
	
	@ApiModelProperty(
			value      = "업로드 예정 패턴 수"
			, name     = "apwlPatCount"
			, dataType = "int"
			, example  = "0")
	private int apwlPatCount = 0;
	
	@ApiModelProperty(
			value      = "업로드 완료 패턴 수"
			, name     = "apwlPatOkCount"
			, dataType = "int"
			, example  = "0")
	private int apwlPatOkCount = 0;
	
	
	// 업로드 대기중  : 50건 이상의 데이터가 존재하며 백그라운드 프로세스 대기중
	// 업로드 완료   : 50건 미만의 데이터 저장시에는 즉시 저장 후 업로드 완료가 되며 50건 이상의 데이터가 백그라운드 프로세스로 인해 저장 완료 후에도 완료 상태가 됨
	// 등록 완료     : 상세페이지에서 패턴 등록이 완료된 상태
	@ApiModelProperty(
			value      = "업로드 처리 상태 - 1 : 업로드 완료, 2 : 업로드 대기중, 3 : 등록 완료"
			, name     = "apwlPatState"
			, dataType = "int"
			, example  = "0")
	private int apwlPatState = 0;
	
	@ApiModelProperty(
			value      = "대기 패턴 일련번호"
			, name     = "apwdSeq"
			, dataType = "int"
			, example  = "0")
	private int apwdSeq = 0;
	
	@ApiModelProperty(
			value      = "패턴"
			, name     = "wordNm"
			, dataType = "String"
			, example  = "")
	private String wordNm = "";
	
	@ApiModelProperty(
			value      = "패턴 일련번호"
			, name     = "patSeq"
			, dataType = "int"
			, example  = "0")
	private int patSeq = 0;
	
	@ApiModelProperty(
			value      = "패턴 분석 결과- 분석기"
			, name     = "patternWord"
			, dataType = "String"
			, example  = "")
	private String patternWord = "";
	
	@ApiModelProperty(
			value      = "패턴 분석 결과 - 사용자"
			, name     = "patternWordUser"
			, dataType = "String"
			, example  = "")
	private String patternWordUser = "";
	
	@ApiModelProperty(
			value      = "패턴 분석 타입                                 "
			, name     = "patternType"
			, dataType = "String"
			, example  = "")
	private String patternType = "";
	
	@ApiModelProperty(
			value      = "등록일"
			, name     = "apwdDate"
			, dataType = "String"
			, example  = "")
	private String apwdDate = "";
	
	@ApiModelProperty(
			value      = "패턴 유형"
			, name     = "patLang"
			, dataType = "String"
			, example  = "")
	private String patLang = "";
	
	@ApiModelProperty(
			value      = "사용 유무"
			, name     = "useYn"
			, dataType = "String"
			, example  = "")
	private String useYn = "";
	
	@ApiModelProperty(
			value      = "부모 일련번호"
			, name     = "pPatSeq"
			, dataType = "int"
			, example  = "0")
	private int pPatSeq = 0;
	
	@ApiModelProperty(
			value      = "부모 패턴"
			, name     = "pWordNm"
			, dataType = "String"
			, example  = "")
	private String pWordNm = "";
	
	@ApiModelProperty(
			value      = "비고"
			, name     = "discription"
			, dataType = "String"
			, example  = "")
	private String discription = "";
	
	@ApiModelProperty(
			value      = "시스템 일련번호"
			, name     = "systemKey"
			, dataType = "int"
			, example  = "0")
	private int systemKey = 0;
	
	@ApiModelProperty(
			value      = "분류 타입"
			, name     = "icType"
			, dataType = "String"
			, example  = "")
	private String icType = "";
	
	@ApiModelProperty(
			value      = "분류 코드"
			, name     = "icCode"
			, dataType = "String"
			, example  = "")
	private String icCode = "";
	
	@ApiModelProperty(
			value      = "상위 분류 타입"
			, name     = "icPType"
			, dataType = "String"
			, example  = "")
	private String icPType = "";
	
	@ApiModelProperty(
			value      = "상위 분류 코드"
			, name     = "icPCode"
			, dataType = "String"
			, example  = "")
	private String icPCode = "";
	
	@ApiModelProperty(
			value      = "가중치"
			, name     = "weightedPoint"
			, dataType = "int"
			, example  = "0")
	private int weightedPoint = 0;
	
	@ApiModelProperty(
			value      = "감성 분석 여부"
			, name     = "sentimentYn"
			, dataType = "String"
			, example  = "")
	private String sentimentYn = "";
	
	@ApiModelProperty(
			value      = "등록 처리 상태  - Y : 등록 완료 , N : 등록 대기"
			, name     = " apwdPatState"
			, dataType = "String"
			, example  = "")
	private String apwdPatState = "";
	 
	@ApiModelProperty(
			value      = "카테고리 수정 여부 - Y : 수정 후   , N : 수정 전"
			, name     = "apwdIcState"
			, dataType = "String"
			, example  = "")
	private String apwdIcState = "";
	
	@ApiModelProperty(
			value      = "POMS 사용자 일련번호"
			, name     = "puSeq"
			, dataType = "int"
			, example  = "0")
	private int puSeq = 0;
	
	@ApiModelProperty(
			value      = "POMS 사용자 이름"
			, name     = "puName"
			, dataType = "String"
			, example  = "")
	private String puName = "";
	
	@ApiModelProperty(
			value      = ""
			, name     = "patternList"
			, dataType = "List"
			, example  = "")
	private List<CategoryWaitingVO> patternList;
	
	
	public void setSearchVo( SearchVo searchVo ){
		
		/** 정렬 **/
		this.orderByField = CategoryWaitingEnum.of(searchVo.getOrderByField()).toString();
		this.orderByValue = searchVo.getOrderByValue();

		/** 검색조건 1 **/
		this.searchField  = CategoryWaitingEnum.of(searchVo.getSearchField()).toString();
		this.searchValue  = searchVo.getSearchValue();

		/** 검색조건 2 **/
		this.searchField1 = CategoryWaitingEnum.of(searchVo.getSearchField1()).toString();
		this.searchValue1 = searchVo.getSearchValue1();
		
		this.seqListField = CategoryWaitingEnum.of(searchVo.getSeqListField()).toString();
		this.seqList      = searchVo.getSeqList();
		
		/** 페이징 **/
		this.limitNum     = searchVo.getLimitNum();
		this.rowNum       = searchVo.getRowNum();
	}
	
 
}
