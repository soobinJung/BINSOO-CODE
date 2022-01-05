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
	 * ����Ʈ ���¿� ���� �����͸� �����´�
	 * 1 = ���ε� �����
	 * 4 = ����ε� �����
	 */
	public List<PatternwaitingVO> getApwlListByApwlPatState( int apwlSeq ) {
		
		log.info("########## GET APWL => APWL_PAT_STATE = 1");
		
		PatternwaitingVO vo = new PatternwaitingVO(); 
		vo.setApwlSeq(apwlSeq);
		return dao.selectAnaPatternWaitingList(vo);
	}
	
	
	
	/**
	 * APWL SEQ �� �������� �׼� ������ ������ �´�.
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
	 * �׼� ������ ���� �Ѵ�.
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
	 * �׼� ������ ��ü ��Ų �� APWD �����͵��� INSERT ��Ų��.
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
	                
	                /** �з� ���� **/
	                if( j == 0 ) {
	                	String icCode = cell.getStringCellValue();
	                	vo.setIcCode(icCode);
	                	
	                	if( map.containsKey(icCode) ) {
	                		map.put(icCode, (map.get(icCode) + 1));
	                	}else {
	                		map.put(icCode, 1);
	                	}
	                }
	                
	                /** ���� Ÿ�� (Null �� ��� ���� ��ü) - G : ����, N : ���� **/
	                if( j == 1 ) {
	                	vo.setPatternType( cell.getStringCellValue().equals("") ? "N" : cell.getStringCellValue());
	                }
	                
	                /** ����� ���� **/
	                if( j == 2 ) {
	                	vo.setWordNm(cell.getStringCellValue());
	                }
	                
	                /** ����� ���Ͻ� **/
	                if( j == 3 ) {
	                	vo.setPatternWordUser(cell.getStringCellValue());
	                }
	                
	                /** ���� ��� **/
	                if( j == 4 ) {
	                	vo.setPatternLang(cell.getStringCellValue());
	                }
	            }
	            
	            /** ���� �м� ��� **/
	            vo.setPatternWord( koreanAnalysis.setAnalysisWord(vo) );
	            patternList.add(vo);
		    }	  
		   
		    
		   /** ���� ������ ���� **/
		   PatternwaitingVO apwdVO = new PatternwaitingVO();
		   apwdVO.setPatternList(patternList);
		   
		   dao.insertAnaPatternWaitingData(apwdVO);
		   
		   
		   /** ���� ��� ������Ʈ ������ **/
		   PatternwaitingVO apwlVO = new PatternwaitingVO(); 
		   
		   List<String> patternTitleList = new ArrayList<>();
		    
		   map.entrySet().forEach( entry -> {
		    	entry.getKey();
		    	entry.getValue();
		    	
		    	patternTitleList.add(entry.getKey() + " " + entry.getValue() + " ��");
		    });
		   
		   apwlVO.setApwlSeq(apwlSeq);
		   apwlVO.setApwlTitle(String.join(", ", patternTitleList));
		   
		   apwlVO.setApwlPatCount(patternList.size());
		   
		   /** 
		    * APWL_PAT_STATE ( ���ε� ó�� ���� )
		    * 
		    * 1 : ���ε� �����
		    * 2 : ���ε� �Ϸ�
		    * 3 : ��� �Ϸ�
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
	 * ���� ����ε� ó�� ����.
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
	                
	                /** �ý��� Ű **/
	                if( j == 0 ) {
	                	vo.setSystemKey((int)cell.getNumericCellValue());
 
	                }
	                
	                /** ���� Ű **/
	                if( j == 1 ) {
	                	vo.setApwdSeq((int)cell.getNumericCellValue());
	                }
	                
	                /** ���� Ÿ�� (Null �� ��� ���� ��ü) - G : ����, N : ����  **/
	                if( j == 2 ) { 
	                	String patternType = cell.getStringCellValue();
	                	vo.setPatternType(patternType.equals("") ? "N" : patternType);
	                }
	                
	                /** ���� **/
	                if( j == 3 ) {
	                	vo.setWordNm(cell.getStringCellValue());
	                }
	                
	                /** ���Ͻ� ���� **/
	                if( j == 4 ) { 
	                	vo.setPatternWordUser(cell.getStringCellValue());
	                }
	            } 
	            PatternwaitingVO orgVO = dao.selectAnaPatternWaitingData(vo);
	            Optional<PatternwaitingVO> op = Optional.ofNullable( orgVO );
 
	            /** �������� �ʴ� �Ϸù�ȣ **/
	            if (!op.isPresent()) {
            	  continue;
            	}
	            
	            
	            /** �����̳� ���� Ÿ���� ����Ǿ��ٸ� **/
	            if(!orgVO.getWordNm().equals(vo.getpWordNm()) || !orgVO.getPatternType().equals(vo.getPatternType())) {
	            	
	            	/**
	            	 *  - ������Ʈ
	            	 *      ���� 
	            	 *      ���� Ÿ��
	            	 *      ���� �м� �м��� ���
	            	 *      ���� �м� ���� ��� 
	            	 **/
		            
		            /** ���� �м��� ��� ������Ʈ **/
		            vo.setPatternWord( koreanAnalysis.setAnalysisWord(vo) );
		            dao.updateAnaPatternWaitingData(vo);
		         
		        /** ����� ���Ͻ��� ���� �Ǿ��ٸ� **/
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
	 * APWL �� COUNT, STATE, TITLE �� �����Ѵ�.
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
	 * PatternwaitingVO �⺻ �� ����
	 */
	private PatternwaitingVO setPatternWaitingVO( int apwlSeq, int puSeq  ) {
		
		 PatternwaitingVO vo = new PatternwaitingVO();
         
         /**  ��� ���� ����Ʈ �Ϸù�ȣ **/
         vo.setApwlSeq(apwlSeq);
         
         /** ���� �Ϸù�ȣ **/
         vo.setPatSeq(0);
         
         /** ��� ���� **/
         vo.setUseYn("Y");
         
         /** �θ� �Ϸù�ȣ **/
         vo.setpPatSeq(0);
         
         /** �θ� ���� **/
         vo.setpWordNm("");
         
         /**POMS ����� �Ϸù�ȣ **/
         vo.setPuSeq(puSeq);
         
         /** ��� **/
         vo.setDiscription("");
         
         /** �ý��� Ű **/
         vo.setSystemKey(0);
         
         /** ����ġ **/
         vo.setWeightedPoint(0);
         
         /** �з� Ÿ�� **/
         vo.setIcType("");
         
         /** �����м� ���� **/
         vo.setSentimentYn("N");
         
         /** ���ε� ó�� ����  - 1 : ��� ��� ���� , 2 : ��� ����, 3 : �ߺ� ���� **/
         vo.setApwdPatState(1);
         
         /** ī�װ� ���� ���� - Y : ���� �� , N : ���� �� **/
         vo.setApwdIcState("N");
         
		return vo;

	}
	
}