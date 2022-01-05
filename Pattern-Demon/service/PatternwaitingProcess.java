package com.rsn.patternwaiting.service;

import org.apache.log4j.Logger;
import com.rsn.patternwaiting.vo.PatternwaitingVO;

public class PatternwaitingProcess {

	protected Logger log = Logger.getLogger(this.getClass());
	
	
	/**
	 * DEVEL
	 */
	public void develProcess() {
		
		log.info("############################################################################################################################################################################");
		log.info("####################################################### [ DEVEL DATA ] - PATTERN UPLOAD DATA ###############################################################################");
		log.info("############################################################################################################################################################################");
		
		PatternwaitingService uploadService = new PatternwaitingService(
				"DEVEL_"
				, "/app/PomsDaemon/PatternWating/Upload/PomsDevel/"
		);
		
		uploadService.getApwlListByApwlPatState(1).forEach( x -> {
			
			PatternwaitingVO apwlVO = uploadService.insertApwdExcelUploadFile( 
					uploadService.getApwlExcelFile( x.getApwlSeq() )
					,  x.getApwlSeq()
					,  x.getPuSeq()
					
			);
			
			uploadService.updateAttributeByApwl( apwlVO );
			uploadService.deleteApwlExcelFile( x.getApwlSeq() );
			
		});
		
		log.info("############################################################################################################################################################################");
		log.info("####################################################### [ DEVEL DATA ] - PATTERN REUPLOAD DATA #############################################################################");
		log.info("############################################################################################################################################################################");
	
		PatternwaitingService reuploadService = new PatternwaitingService(
				"DEVEL_"
				, "/app/PomsDaemon/PatternWating/Reupload/PomsDevel/"
		);
		
		reuploadService.getApwlListByApwlPatState(4).forEach( x -> {
			
			reuploadService.insertApwdExcelReuploadFile( 
						reuploadService.getApwlExcelFile(x.getApwlSeq())
						, x.getApwlSeq()
						,  0
			);
			
			x.setApwlPatState(2);
			uploadService.updateAttributeByApwl( x );
			uploadService.deleteApwlExcelFile( x.getApwlSeq() );
			
		});
	}


	
	
	/**
	 * OPERATION
	 */
	public void operationProcess() {
		
		log.info("############################################################################################################################################################################");
		log.info("####################################################### [ OPERATION DATA ] - PATTERN UPLOAD DATA ###########################################################################");
		log.info("############################################################################################################################################################################");
 
		
		PatternwaitingService uploadService = new PatternwaitingService(
				""
				, "/app/PomsDaemon/PatternWating/Upload/PomsOperation/"
		);

		uploadService.getApwlListByApwlPatState(1).forEach( x -> {
			
			PatternwaitingVO apwlVO = uploadService.insertApwdExcelUploadFile( 
					uploadService.getApwlExcelFile( x.getApwlSeq() )
					,  x.getApwlSeq()
					,  x.getPuSeq()
					
			);
			
			uploadService.updateAttributeByApwl( apwlVO );
			uploadService.deleteApwlExcelFile( x.getApwlSeq() );
		});
		

		log.info("############################################################################################################################################################################");
		log.info("####################################################### [ OPERATION DATA ] - PATTERN REUPLOAD DATA #########################################################################");
		log.info("############################################################################################################################################################################");
 
		PatternwaitingService reuploadService = new PatternwaitingService(
				""
				, "/app/PomsDaemon/PatternWating/Reupload/PomsOperation/"
		);
		
		reuploadService.getApwlListByApwlPatState(4).forEach( x -> {
			
			reuploadService.insertApwdExcelReuploadFile( 
						reuploadService.getApwlExcelFile(x.getApwlSeq())
						, x.getApwlSeq()
						,  0
			);
			
			x.setApwlPatState(2);
			uploadService.updateAttributeByApwl( x );
			uploadService.deleteApwlExcelFile( x.getApwlSeq() );
			
		});
	}
}
