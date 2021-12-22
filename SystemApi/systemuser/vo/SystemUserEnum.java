 package com.rsn.POMS.api.poms.systemuser.vo;

import java.util.Arrays;
import com.rsn.POMS.api.setting.common.enumCode.EnumMapperType;

/**
 * @create @author 정수빈 @Date 2021.12
 */

public enum SystemUserEnum implements EnumMapperType {
	
	systemKey("systemKey")
	, pu_seq("puSeq")
	, pu_reg_timestamp("puRegTimestamp");

	private String title;

	SystemUserEnum(String title) {
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
