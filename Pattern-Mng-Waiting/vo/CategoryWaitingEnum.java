package com.rsn.POMS.api.dictionary.advanced.categorywaiting.vo;

import java.util.Arrays;

import com.rsn.POMS.api.setting.common.enumCode.EnumMapperType;

public enum CategoryWaitingEnum implements EnumMapperType  {
	
	apwl_seq("apwlSeq")
	, apwl_title("apwlTitle")
	, apwl_date("apwlDate")
	, apwl_pat_count("apwlPatCount")
	, apwl_pat_state("apwlPatState")
	, apwd_seq("apwdSeq")
	, pat_seq("patSeq")
	, word_nm("wordNm")
	, pattern_word("patternWord")
	, pattern_word_user("patternWordUser")
	, pattern_type("patternType")
	, apwd_date("apwdDate")
	, pat_lang("patLang")
	, use_yn("useYn")
	, p_pat_seq("pPatSeq")
	, p_word_nm("pWordNm")
	, description("discription")
	, system_key("systemKey") 
	, ic_seq("icSeq")
	, ic_type("icType")
	, ic_code("icCode")
	, weighted_point("weightedPoint")
	, sentiment_yn("sentimentYn")
	, apwd_pat_state("apwdPatState")
	, apwd_ic_state("apwdIcState")
	, pu_seq("puSeq")
	;
	
	private String title;

	CategoryWaitingEnum(String title) {
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
