package com.rsn.patternwaiting.job;

import com.rsn.common.excutor.ExcutorAbstract; 
import com.rsn.patternwaiting.service.PatternwaitingProcess;

/*
 * @Author sbjung@realsn.com
 * @Date   2021 12 17
 */
public class Patternwaiting extends ExcutorAbstract {

	private PatternwaitingProcess process;
	
	public Patternwaiting() {
		process = new PatternwaitingProcess();
	}
	

	@Override
	public void run() throws Exception {
		
		/**
		 * DEVEL DATA CHECK
		 */
		process.develProcess();
		
		/**
		 * OPERATION DATA CHECK
		 */
		process.operationProcess();
	}
	
	
	public static void main(String[] args) {
		
		Patternwaiting job = new Patternwaiting();
		job.execute("010-8478-6861");
	}
}
