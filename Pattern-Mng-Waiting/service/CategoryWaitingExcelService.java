package com.rsn.POMS.api.dictionary.advanced.categorywaiting.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.rsn.POMS.api.dictionary.advanced.categorywaiting.dao.CategoryWaitingDao;
import com.rsn.POMS.api.dictionary.advanced.categorywaiting.vo.CategoryWaitingVO;
import com.rsn.POMS.api.setting.exception.CustomException;
import com.rsn.POMS.api.setting.utils.ApiResultErrorCode;
import com.rsn.POMS.db.ConfigPath;
import com.rsn.POMS.security.utils.TokenUtils;

@Service
public class CategoryWaitingExcelService {

	@Autowired
	private CategoryWaitingDao dao;
	
	@Autowired
	private TokenUtils tokenUtils;
	 
	
	@Value("${server.port}")
	int SERVER_PORT;
	
	private static String FILE_NAME = "";
	private static String LOCAL_PATH = "";
	
	private static String WINDOW_PATH = "";
	private static String LINUX_PATH = "";
	private static String SERVER_PATH = "";
	
	@Autowired
	public CategoryWaitingExcelService() {
		
		this.WINDOW_PATH = System.getProperty("user.dir") + "\\src\\main\\webapp\\WEB-INF\\file\\" ;
		this.LINUX_PATH  = "/www/POMS/temp_file/";
		this.SERVER_PATH = "/app/PomsDaemon/" 
                            + ( new ConfigPath().getNAME_TABLE().equals("DEVEL_") ? "Poms_Devel" : "Poms_Operation" ); 
	}
	
	
	public String fileProcessingByPatternWaiting( MultipartFile multipartFile, HttpServletRequest request ) {
		
		if (multipartFile.isEmpty()) { 
			new CustomException(
					ApiResultErrorCode.NoDataException
					, " [ PatternWaitingExcelService ] Excel file doesn't exist."
		    );
		}
		
		try {
			
			/** ????????? ?????? ?????? **/
			FILE_NAME = "PATTERN_WAITING_" + insertPatternWaitingList() + ".xlsx";
			
			/** ?????? ????????? ?????? ?????? ?????? **/
			localSaveBypatternFile(multipartFile);
			
			/** ?????? ????????? ?????? ?????? ?????? **/
			operationSaveBypatternFile();
			
			/** ?????? ????????? ?????? ?????? ?????? **/
			localDeleteBypatternFile();
			
		} catch (IllegalStateException e) {
			System.out.println( "[ PatternWaitingExcelService - IllegalStateException ]"  + e);
			e.printStackTrace();
			
		} catch (IOException e) {
			System.out.println( "[ PatternWaitingExcelService - IOException ]"  + e);
			e.printStackTrace();
		}
		
		return FILE_NAME;
	}
	
	
	/**
	 *  ?????? ?????? ??????
	 */
	private int insertPatternWaitingList() {
		return dao.insertPatternWaitingList( 
				CategoryWaitingVO.builder()
		    		.apwlTitle("-")
		    		.puSeq(tokenUtils.getAuthUser().getPu_seq())
		    		.apwlPatCount(0)
		    		.useYn("Y")
		    		.apwlPatState(1)
		    		.build()
				).getAPWL_SEQ();
	}
	
	
	/**
	 * ????????? Local ??? ??????
	 */
	private void localSaveBypatternFile( MultipartFile multipartFile ) throws IllegalStateException, IOException {
		LOCAL_PATH  = (SERVER_PORT == 80)     ? WINDOW_PATH + FILE_NAME
					 : (SERVER_PORT == 9191)  ? LINUX_PATH  + FILE_NAME
					 : "";
		
		multipartFile.transferTo( new File( LOCAL_PATH ) );
	}
	
	
	/**
	 * ????????? ??????????????? ??????
	 */
	@SuppressWarnings("finally")
	private String operationSaveBypatternFile() {
		
	    try {
	      JSch jsch = new JSch();

	      /** ????????? ?????????????????? ?????? ?????? ?????? **/
	      final String  USER        = "root";
	      final String  PWD         = "dkfdptmdps404";
	      final String  IP          = "61.110.19.86";
	      final String  PROTOCOL    = "sftp";
	      final Integer PORT        = 22;
	      
	      Session session = jsch.getSession(
	    		  USER
	    		  , IP
	    		  , PORT );
	      
	      session.setConfig("StrictHostKeyChecking", "no");
	      session.setPassword(PWD);  
	      session.connect();
	      
	      Channel channel = session.openChannel(PROTOCOL);
	      channel.connect();
	      
	      ChannelSftp sftpChannel = (ChannelSftp) channel;
	      sftpChannel.cd(SERVER_PATH); 
	      
	      File file = new File( LOCAL_PATH );
	      
	      FileInputStream fis = new FileInputStream( file );
	      
	      sftpChannel.put(fis, file.getName());
	      fis.close();

	      sftpChannel.disconnect();
	      channel.disconnect();
	      session.disconnect(); 
	      
	    } catch( JSchException e ) { 
	    	System.out.println( "[ PatternWaitingExcelService - JSchException ]"  + e);
	    	e.printStackTrace();
	    } catch( IOException e ) { 
	    	System.out.println( "[ PatternWaitingExcelService - IOException ]"  + e);
	    	e.printStackTrace();
	    } catch( SftpException e ) {
	    	System.out.println( "[ PatternWaitingExcelService - SftpException ]"  + e);
	    	e.printStackTrace();
	    }finally {
	    	return FILE_NAME;
	    } 
	}
	
	
	/**
	 * ?????? ?????? ????????? ??????
	 */
	private void localDeleteBypatternFile(){
		File file = new File(LOCAL_PATH);
		file.delete();
	}
	
}
