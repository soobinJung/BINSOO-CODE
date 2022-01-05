package com.rsn.patternwaiting.vo;

import org.apache.log4j.Logger;

public class PatternwaitingInfo {

	protected Logger log = Logger.getLogger(this.getClass());
	
	/***** 
	 * - �
	 * STATUS = true
	 * - ����
	 * STATUS = false
	 */
	private boolean STATUS = false;
	
	public String ENV       = "";
	public String FILE_PATH_COMMON = "";
	
	/*****
	 * ���� ����� �ܺθ� - lucy2_outter
	 * ���� ������ ���θ� - lucy2_inner
	 */
	public PatternwaitingInfo() {
		log.info("#### LUCY PATTERN WAITING DEMON PROCESS");
		
		/***
		 * �� �� 
		 * ������ ���θ����� �����ؾ� �ϴµ� �ܺθ����� �ؾ� �����
		 */
		if(STATUS) {
			log.info("#### �� �� - �ܺθ� ����");
			this.ENV = "lucy2_outter";
		}
		
		/***	
		 * TEST
		 * - �ܺθ� 
		 */
		if(!STATUS) {
			log.info("#### TEST - �ܺθ� ����");
			this.ENV = "lucy2_outter";
		}
	}
}
