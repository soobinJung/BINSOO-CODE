package com.rsn.POMS.api.poms.system.vo;

import java.util.Arrays;

import com.rsn.POMS.api.setting.common.enumCode.EnumMapperType;

public enum SystemEnum  implements EnumMapperType {

	systemKey("systemKey")
	, system("system")
	, pu_seq("puSeq")
	, admin1("admin1")
	, admin2("admin2")
	, domain("domain")
	, host("host")
	, host2("host2")
	, auto_anal("autoAnal")
	, issue_data("issueData")
	, client("client")
	, system_use("systemUse")
	, sys_reg_timestamp("sysRegTimestamp");

	private String title;

	SystemEnum(String title) {
		this.title = title;
	}

	@Override
	public String getCode() {
		return name();
	}

	@Override
	public String getTitle() {
		return title;
	}

	
	/**
	 * code 에 JAVA 표기법 컬럼명을 보내주면 SQL 표기법 컬럼명으로 바꿔 반환 합니다.
	 * 없는 컬럼명이라면 "" 반환
	 * 
	 * [ EX ]
	 * code   = puSeq
	 * return = pu_seq
	 */
	static String of(String code) {
		
		if( code == "" ) {
			return "";
		}
		
		return Arrays.stream( values())
				              .filter(v -> code.equals(v.title) )
				              .findFirst()
				              .orElseThrow( () -> new IllegalArgumentException(String.format("%s는 사용할 수 없는 컬럼 입니다.", code))).toString();
	}
}