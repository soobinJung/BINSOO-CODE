package com.rsn.patternwaiting.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.rsn.patternwaiting.dao.PatternwaitingDao;
import com.rsn.patternwaiting.vo.PatternwaitingVO;

public class PatternwaitingService {

	protected Logger log = Logger.getLogger(this.getClass());
	
	private String EXCEL_FILE_NAME = "PATTERN_WAITING_";
	private String DB_TABLE_NAME;
	private String SERVER_PATH;
	
	
	private PatternwaitingDao dao;
	private KoreanAnalysis koreanAnalysis; 
			
	public PatternwaitingService( String DB_TABLE_NAME, String SERVER_PATH ) {
		
		this.DB_TABLE_NAME = DB_TABLE_NAME;
		this.SERVER_PATH = SERVER_PATH;
		
		this.dao = new PatternwaitingDao(DB_TABLE_NAME);
		this.koreanAnalysis = new KoreanAnalysis();
	}
 
	
	
	/**
	 * 리스트 상태에 따라서 데이터를 가져온다
	 * 1 = 업로드 대기중
	 * 4 = 재업로드 대기중
	 */
	public List<PatternwaitingVO> getApwlListByApwlPatState( int apwlSeq ) {
		
		log.info("########## GET APWL => APWL_PAT_STATE = 1");
		
		PatternwaitingVO vo = new PatternwaitingVO(); 
		vo.setApwlSeq(apwlSeq);
		return dao.selectAnaPatternWaitingList(vo);
	}
	
	
	
	/**
	 * APWL SEQ 를 기준으로 액셀 파일을 가지고 온다.
	 */
	public File getApwlExcelFile( int apwlSeq ) {
		
		String path = this.SERVER_PATH + this.EXCEL_FILE_NAME + apwlSeq + ".xlsx";
		
		/** TEST **/
//		 path = "C:\\Users\\soobin\\Documents\\TEST.xlsx";
		
		File file = new File( path );
		
		log.info("########## GET FILE ##########");
		log.info("########## GET FILE PATH  >>> " + path);
		
		return file; 
		
	}
	
	
	/**
	 * 액셀 파일을 삭제 한다.
	 */
	public File deleteApwlExcelFile( int apwlSeq ) {
		
		String path = this.SERVER_PATH + this.EXCEL_FILE_NAME + apwlSeq + ".xlsx";
		
		/** TEST **/
//		 path = "C:\\Users\\soobin\\Documents\\BINSOOTEST.xlsx";
		
		File file = new File( path );
		boolean status = file.delete();
		
		log.info("########## DELETE FILE ##########");
		log.info("########## DELETE FILE PATH  >>> " + path);
		log.info("########## DELETE FILE STATE >>> " + status);
		
		return file; 
		
	}
	
	
	/**
	 * 액셀 파일을 해체 시킨 후 APWD 데이터들을 INSERT 시킨다.
	 */
	@SuppressWarnings({ "unchecked", "resource" })
	public PatternwaitingVO insertApwdExcelUploadFile( File file, int apwlSeq, int puSeq ) {

		log.info("########## INSERT APWD EXCEL FILE ");
		
		List<PatternwaitingVO> patternList = new ArrayList<>();
		Map<String, Integer> map = new HashedMap();
		
		try {
			
			Workbook workbook = new  XSSFWorkbook( file ); 
			Sheet workSheet = workbook.getSheetAt(0);
			
		    int rowNum = workSheet.getPhysicalNumberOfRows();
		    
		    for(int i = 1 ; i < rowNum ; i++){
		    	
	            Row row = workSheet.getRow(i);
	            PatternwaitingVO vo =  setPatternWaitingVO( apwlSeq, puSeq  );
	            
	            for(int j = 0 ; j < row.getPhysicalNumberOfCells() ; j ++){
	            	
	                Cell cell = row.getCell(j);
	                
	                /** 분류 정보 **/
	                if( j == 0 ) {
	                	String icCode = cell.getStringCellValue();
	                	vo.setIcCode(icCode);
	                	
	                	if( map.containsKey(icCode) ) {
	                		map.put(icCode, (map.get(icCode) + 1));
	                	}else {
	                		map.put(icCode, 1);
	                	}
	                }
	                
	                /** 패턴 타입 (Null 값 허용 인접 대체) - G : 고유, N : 인접 **/
	                if( j == 1 ) {
	                	vo.setPatternType( cell.getStringCellValue().equals("") ? "N" : cell.getStringCellValue());
	                }
	                
	                /** 사용자 패턴 **/
	                if( j == 2 ) {
	                	vo.setWordNm(cell.getStringCellValue());
	                }
	                
	                /** 사용자 패턴식 **/
	                if( j == 3 ) {
	                	vo.setPatternWordUser(cell.getStringCellValue());
	                }
	                
	                /** 패턴 언어 **/
	                if( j == 4 ) {
	                	vo.setPatternLang(cell.getStringCellValue());
	                }
	            }
	            
	            /** 패턴 분석 결과 **/
	            vo.setPatternWord( koreanAnalysis.setAnalysisWord(vo) );
	            patternList.add(vo);
		    }	  
		   
		    
		   /** 패턴 데이터 삽입 **/
		   PatternwaitingVO apwdVO = new PatternwaitingVO();
		   apwdVO.setPatternList(patternList);
		   
		   dao.insertAnaPatternWaitingData(apwdVO);
		   
		   
		   /** 패턴 목록 업데이트 데이터 **/
		   PatternwaitingVO apwlVO = new PatternwaitingVO(); 
		   
		   List<String> patternTitleList = new ArrayList<>();
		    
		   map.entrySet().forEach( entry -> {
		    	entry.getKey();
		    	entry.getValue();
		    	
		    	patternTitleList.add(entry.getKey() + " " + entry.getValue() + " 건");
		    });
		   
		   apwlVO.setApwlSeq(apwlSeq);
		   apwlVO.setApwlTitle(String.join(", ", patternTitleList));
		   
		   apwlVO.setApwlPatCount(patternList.size());
		   
		   /** 
		    * APWL_PAT_STATE ( 업로드 처리 상태 )
		    * 
		    * 1 : 업로드 대기중
		    * 2 : 업로드 완료
		    * 3 : 등록 완료
		    */
		   apwlVO.setApwlPatState(2);
		   
		   
		   return apwlVO;
		    
		} catch (IOException e) {
 			e.printStackTrace();
		
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
		
		return null;  
	}

	
	
	
	/*
	 * 패턴 재업로드 처리 로직.
	 */
	@SuppressWarnings({"resource"})
	public void insertApwdExcelReuploadFile( File file, int apwlSeq, int puSeq ) {

		log.info("########## INSERT APWD REUPLOAD EXCEL FILE ");
		
		try {
			
			Workbook workbook = new  XSSFWorkbook( file ); 
			Sheet workSheet = workbook.getSheetAt(0);
			
		    int rowNum = workSheet.getPhysicalNumberOfRows();
		    
		    for(int i = 1 ; i < rowNum ; i++){
		    	
	            Row row = workSheet.getRow(i);
	            PatternwaitingVO vo =  setPatternWaitingVO( apwlSeq, puSeq  );
	            
	            for(int j = 0 ; j < row.getPhysicalNumberOfCells() ; j ++){
	            	
	                Cell cell = row.getCell(j);
	                
	                /** 시스템 키 **/
	                if( j == 0 ) {
	                	vo.setSystemKey((int)cell.getNumericCellValue());
 
	                }
	                
	                /** 패턴 키 **/
	                if( j == 1 ) {
	                	vo.setApwdSeq((int)cell.getNumericCellValue());
	                }
	                
	                /** 패턴 타입 (Null 값 허용 인접 대체) - G : 고유, N : 인접  **/
	                if( j == 2 ) { 
	                	String patternType = cell.getStringCellValue();
	                	vo.setPatternType(patternType.equals("") ? "N" : patternType);
	                }
	                
	                /** 패턴 **/
	                if( j == 3 ) {
	                	vo.setWordNm(cell.getStringCellValue());
	                }
	                
	                /** 패턴식 수동 **/
	                if( j == 4 ) { 
	                	vo.setPatternWordUser(cell.getStringCellValue());
	                }
	            } 
	            PatternwaitingVO orgVO = dao.selectAnaPatternWaitingData(vo);
	            Optional<PatternwaitingVO> op = Optional.ofNullable( orgVO );
 
	            /** 존재하지 않는 일련번호 **/
	            if (!op.isPresent()) {
            	  continue;
            	}
	            
	            
	            /** 패턴이나 패턴 타입이 변경되었다면 **/
	            if(!orgVO.getWordNm().equals(vo.getpWordNm()) || !orgVO.getPatternType().equals(vo.getPatternType())) {
	            	
	            	/**
	            	 *  - 업데이트
	            	 *      패턴 
	            	 *      패턴 타입
	            	 *      패턴 분석 분석기 결과
	            	 *      패턴 분석 수동 결과 
	            	 **/
		            
		            /** 패턴 분석기 결과 업데이트 **/
		            vo.setPatternWord( koreanAnalysis.setAnalysisWord(vo) );
		            dao.updateAnaPatternWaitingData(vo);
		         
		        /** 사용자 패턴식이 변경 되었다면 **/
	            }else if(!orgVO.getPatternWordUser().equals(vo.getPatternWordUser())) {
	            	dao.updateAnaPatternWaitingData(vo);
	            }
		    }	  
		    
		} catch (IOException e) {
 			e.printStackTrace();
		
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
	}

	
	
	/**
	 * APWL 의 COUNT, STATE, TITLE 을 수정한다.
	 */
	public PatternwaitingVO updateAttributeByApwl( PatternwaitingVO apwlVO ) {
		
		log.info("########## UPDATE APWL ATTURIBUTE ");
		log.info("##  ApwlSeq      : " + apwlVO.getApwlSeq());
		log.info("##  ApwlTitle    : " + apwlVO.getApwlTitle());
		log.info("##  ApwlPatCount : " + apwlVO.getApwlPatCount());
		log.info("##  ApwlPatState : " + apwlVO.getApwlPatState());
		
		
		return dao.updateAnaPatternWaitingList(apwlVO);

	}
	
	/**
	 * PatternwaitingVO 기본 값 설정
	 */
	private PatternwaitingVO setPatternWaitingVO( int apwlSeq, int puSeq  ) {
		
		 PatternwaitingVO vo = new PatternwaitingVO();
         
         /**  대기 패턴 리스트 일련번호 **/
         vo.setApwlSeq(apwlSeq);
         
         /** 패턴 일련번호 **/
         vo.setPatSeq(0);
         
         /** 사용 유무 **/
         vo.setUseYn("Y");
         
         /** 부모 일련번호 **/
         vo.setpPatSeq(0);
         
         /** 부모 패턴 **/
         vo.setpWordNm("");
         
         /**POMS 사용자 일련번호 **/
         vo.setPuSeq(puSeq);
         
         /** 비고 **/
         vo.setDiscription("");
         
         /** 시스템 키 **/
         vo.setSystemKey(0);
         
         /** 가중치 **/
         vo.setWeightedPoint(0);
         
         /** 분류 타입 **/
         vo.setIcType("");
         
         /** 감성분석 여부 **/
         vo.setSentimentYn("N");
         
         /** 업로드 처리 상태  - 1 : 등록 대기 상태 , 2 : 등록 상태, 3 : 중복 상태 **/
         vo.setApwdPatState(1);
         
         /** 카테고리 수정 여부 - Y : 수정 후 , N : 수정 전 **/
         vo.setApwdIcState("N");
         
		return vo;

	}
	
}