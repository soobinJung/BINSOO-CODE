package com.rsn.patternwaiting.vo;

import org.apache.log4j.Logger;

public class PatternwaitingInfo {

	protected Logger log = Logger.getLogger(this.getClass());
	
	/***** 
	 * - 운영
	 * STATUS = true
	 * - 개발
	 * STATUS = false
	 */
	private boolean STATUS = false;
	
	public String ENV       = "";
	public String FILE_PATH_COMMON = "";
	
	/*****
	 * 로컬 실행시 외부망 - lucy2_outter
	 * 서버 배포시 내부망 - lucy2_inner
	 */
	public PatternwaitingInfo() {
		log.info("#### LUCY PATTERN WAITING DEMON PROCESS");
		
		/***
		 * 배 포 
		 * 원래는 내부망으로 접속해야 하는데 외부망으로 해야 실행됨
		 */
		if(STATUS) {
			log.info("#### 배 포 - 외부망 접속");
			this.ENV = "lucy2_outter";
		}
		
		/***	
		 * TEST
		 * - 외부망 
		 */
		if(!STATUS) {
			log.info("#### TEST - 외부망 접속");
			this.ENV = "lucy2_outter";
		}
	}
}
