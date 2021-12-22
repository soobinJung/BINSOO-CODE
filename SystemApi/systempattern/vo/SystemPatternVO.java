package com.rsn.POMS.api.poms.systempattern.vo;

import com.rsn.POMS.api.setting.common.vo.SearchVo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @create @author 정수빈 @Date 2021.12
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemPatternVO extends SearchVo {

	int system_pattern_seq = 0;
	
	@ApiModelProperty(
			value    = "일련번호", 
			name     = "systemPatternSeq", 
			dataType = "int", 
			example  = "1")
	private int systemPatternSeq;
	
	
	@ApiModelProperty(
			value    = "API_SYSTEM 일련번호", 
			name     = "systemKey", 
			dataType = "int", 
			example  = "17")
	private int systemKey;
	
	
	@ApiModelProperty(
			value    = "ANA_ISSUE_CODE 일련번호", 
			name     = "icSeq", 
			dataType = "int", 
			example  = "1")
	private int icSeq;
	
	
	@ApiModelProperty(
			value    = "분류 타입", 
			name     = "icType", 
			dataType = "int", 
			example  = "1")
	private int icType;
	
	@ApiModelProperty(
			value    = "사용 유무", 
			name     = "useYn", 
			dataType = "String", 
			example  = "y")
	private String useYn;
	
	@ApiModelProperty(
			value    = "등록일", 
			name     = "regDate", 
			dataType = "String", 
			example  = "")
	private String regDate; 
	
	
	/*
	 * 검색 조건 들을 세팅 합니다.
	 * - Total Cnt 조건을 위한 메서드 입니다.
	 */
	public void setSearchVo( SearchVo searchVo ){
		
		this.searchField = SystemPatternEnum.of(searchVo.getSearchField()).toString();
		this.searchValue = searchVo.getSearchValue();
		
		this.searchField1 = SystemPatternEnum.of(searchVo.getSearchField1()).toString();
		this.searchValue1 = searchVo.getSearchValue1();
		
		this.searchField3  = SystemPatternEnum.of(searchVo.getSearchField3()).toString();
		this.searchValue3  = searchVo.getSearchValue3();
		
		this.seqListField  = SystemPatternEnum.of(searchVo.getSeqListField()).toString();
		this.seqList       = searchVo.getSeqList();
		
		this.orderByField  = SystemPatternEnum.of(searchVo.getOrderByField()).toString();
		this.orderByValue  = searchVo.getOrderByValue();
		
		this.rowNum   = searchVo.getRowNum();
		this.limitNum = searchVo.getLimitNum();
	}
}
