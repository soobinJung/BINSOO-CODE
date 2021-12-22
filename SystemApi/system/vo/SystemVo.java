package com.rsn.POMS.api.poms.system.vo;

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
public class SystemVo extends SearchVo {
	
	
	@ApiModelProperty(
			value    = "시스템 일련번호", 
			name     = "systemKey", 
			dataType = "int", 
			example  = "0")
	private int systemKey;

	
	@ApiModelProperty(
			value    = "사용자 일련번호", 
			name     = "puSeq", 
			dataType = "int", 
			example  = "0")
	private int puSeq;
	
	
	@ApiModelProperty(
			value    = "시스템명", 
			name     = "system", 
			dataType = "String", 
			example  = "")
	private String system;


	@ApiModelProperty(
			value    = "관리자1", 
			name     = "admin1", 
			dataType = "String", 
			example  = "")
	private String admin1;

	

	@ApiModelProperty(
			value    = "관리자2", 
			name     = "admin2", 
			dataType = "String", 
			example  = "")
	private String admin2;

	
	
	@ApiModelProperty(
			value    = "도메인", 
			name     = "domain", 
			dataType = "String", 
			example  = "")
	private String domain;

	

	@ApiModelProperty(
			value    = "호스트 주소1", 
			name     = "host", 
			dataType = "String", 
			example  = "")
	private String host;

	
	
	@ApiModelProperty(
			value    = "호스트 주소2", 
			name     = "host2", 
			dataType = "String", 
			example  = "")
	private String host2;

	

	@ApiModelProperty(
			value    = "비고", 
			name     = "description", 
			dataType = "String", 
			example  = "")
	private String description;

	

	@ApiModelProperty(
			value    = "자동분석여부", 
			name     = "autoAnal", 
			dataType = "String", 
			example  = "")
	private String autoAnal;

	

	@ApiModelProperty(
			value    = "", 
			name     = "issueData", 
			dataType = "String", 
			example  = "")
	private String issueData;

	
	
	@ApiModelProperty(
			value    = "", 
			name     = "client", 
			dataType = "String", 
			example  = "")
	private String client;
	

	@ApiModelProperty(
			value    = "시스템 계정 사용여부 (N: 미사용, Y: 사용)", 
			name     = "systemUse", 
			dataType = "String", 
			example  = "")
	private String systemUse;

	
	@ApiModelProperty(
			value    = "시스템 생성일", 
			name     = "sysRegTimestamp", 
			dataType = "int", 
			example  = "1525132800")
	private int sysRegTimestamp;

	@ApiModelProperty(
			value    = "시스템 - 유저 생성일", 
			name     = "puRegTimestamp", 
			dataType = "int", 
			example  = "1525132800")
	private int puRegTimestamp;
	
	
	/*
	 * 검색 조건 들을 세팅 합니다.
	 * - Total Cnt 조건을 위한 메서드 입니다.
	 */
	public void setSearchVo( SearchVo searchVo ){
		
		this.searchField = SystemEnum.of(searchVo.getSearchField()).toString();
		this.searchValue = searchVo.getSearchValue();
		
		this.searchField1 = SystemEnum.of(searchVo.getSearchField1()).toString();
		this.searchValue1 = searchVo.getSearchValue1();
		
		this.searchField3  = SystemEnum.of(searchVo.getSearchField3()).toString();
		this.searchValue3  = searchVo.getSearchValue3();
		
		this.seqListField  = SystemEnum.of(searchVo.getSeqListField()).toString();
		this.seqList  = searchVo.getSeqList();
		
		this.orderByField  = SystemEnum.of(searchVo.getOrderByField()).toString();
		this.orderByValue  = searchVo.getOrderByValue();
		
		this.rowNum = searchVo.getRowNum();
		this.limitNum = searchVo.getLimitNum();
	}
}
