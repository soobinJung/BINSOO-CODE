package com.rsn.POMS.api.dictionary.advanced.categorywaiting.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.rsn.POMS.api.dictionary.advanced.categorywaiting.service.CategoryWaitingExcelService;
import com.rsn.POMS.api.dictionary.advanced.categorywaiting.service.CategoryWaitingService;
import com.rsn.POMS.api.dictionary.advanced.categorywaiting.vo.CategoryWaitingVO;
import com.rsn.POMS.api.setting.common.vo.SearchVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @create @author 정수빈 @Date 2021.05
 */
@RestController
@Api(tags = {"Advanced_Category Waiting"})
@RequestMapping("/api/advanced/category/waiting")
public class CategoryWaitingController {
	
	@Autowired
	private CategoryWaitingService service;
	
	@Autowired
	private CategoryWaitingExcelService excelService;
	
	
	@ApiOperation(
    		value   = "패턴 대기 리스트" 
    		, notes = "패턴 대기 리스트를 반환 합니다.")
    @GetMapping(value = "/list")
    public List<CategoryWaitingVO> selectPatternWaitingList( 
    		
    		SearchVo SearchVo
    		
    		, @ApiParam( 
					name          = "apwlPatState" 
					, type        = "int"
					, value       = "업로드 처리 상태 - 1 : 업로드 완료, 2 : 업로드 대기중, 3 : 등록 완료"
					, example     = "0"
					, required    = false)
			@RequestParam(value = "apwlPatState", defaultValue = "0", required = false) 
			@Nullable int apwlPatState  
			
			, @ApiParam( 
					name          = "useYn" 
					, type        = "String"
					, value       = "카테고리 사용여부"
					, example     = ""
					, required    = false)
			@RequestParam(value = "useYn", defaultValue = "", required = false) 
			@Nullable String useYn 
			
			) {
		
		CategoryWaitingVO vo = CategoryWaitingVO.builder()
								.apwlPatState(apwlPatState)
								.useYn(useYn)
								.build();
		vo.setSearchVo(SearchVo);
		return service.selectPatternWaitingList(vo);
    }
	
	
	
	@ApiOperation(
    		value   = "패턴 대기 리스트 Total Cnt" 
    		, notes = "패턴 대기 리스트 Total Cnt를 반환 합니다.")
    @GetMapping(value = "/list/total-cnt")
    public CategoryWaitingVO selectPatternWaitingListTotalCnt(
    		@ApiParam( 
					name          = "apwlPatState" 
					, type        = "String"
					, value       = "업로드 처리 상태 - 1 : 업로드 완료, 2 : 업로드 대기중, 3 : 등록 완료"
					, example     = "0"
					, required    = false)
			@RequestParam(value = "apwlPatState", defaultValue = "0", required = false) 
			@Nullable int apwlPatState  
			
			, @ApiParam( 
					name          = "useYn" 
					, type        = "String"
					, value       = "카테고리 사용여부"
					, example     = ""
					, required    = false)
			@RequestParam(value = "useYn", defaultValue = "", required = false) 
			@Nullable String useYn 
			
			) {
		
		CategoryWaitingVO vo = CategoryWaitingVO.builder()
								.apwlPatState(apwlPatState)
								.useYn(useYn)
								.build();
		return service.selectPatternWaitingListTotalCnt(vo);
    }
	
	
	@ApiOperation(
    		value   = "패턴 대기 데이터" 
    		, notes = "패턴 대기 데이터를 반환 합니다.")
    @GetMapping(value = "/data")
    public List<CategoryWaitingVO> selectPatternWaitingData( 
    		
    		SearchVo SearchVo
    		
    		, @ApiParam( 
					name          = "apwlSeq" 
					, type        = "int"
					, value       = "패턴 대기 리스트 일련번호"
					, example     = "0"
					, required    = false)
			@RequestParam(value = "apwlSeq", defaultValue = "0", required = false) 
			@Nullable int apwlSeq 
			
			, @ApiParam( 
					name          = "apwdIcState" 
					, type        = "String"
					, value       = "카테고리 수정 여부 - Y : 수정 후   , N : 수정 전"
					, example     = "Y"
					, required    = false)
			@RequestParam(value = "apwdIcState", defaultValue = "", required = false) 
			@Nullable String apwdIcState 
			
			, @ApiParam( 
					name          = "apwdPatState" 
					, type        = "String"
					, value       = "업로드 처리 상태  - Y : 업로드 완료 , N : 업로드 대기중"
					, example     = "Y"
					, required    = false)
			@RequestParam(value = "apwdPatState", defaultValue = "", required = false) 
			@Nullable String apwdPatState 
			
			, @ApiParam( 
					name          = "icCode" 
					, type        = "String"
					, value       = "카테고리 코드"
					, example     = ""
					, required    = false)
			@RequestParam(value = "icCode", defaultValue = "", required = false) 
			@Nullable String icCode 
			
			, @ApiParam( 
					name          = "useYn" 
					, type        = "String"
					, value       = "카테고리 사용여부"
					, example     = ""
					, required    = false)
			@RequestParam(value = "useYn", defaultValue = "", required = false) 
			@Nullable String useYn 
			
			) {
		CategoryWaitingVO vo = CategoryWaitingVO.builder()
								.apwlSeq(apwlSeq)
								.apwdIcState(apwdIcState)
								.apwdPatState(apwdPatState)
								.icCode(icCode)
								.useYn(useYn)
								.build();
		
		System.out.println("apwdIcState    " + apwdIcState);
		vo.setSearchVo(SearchVo);
		return service.selectPatternWaitingData(vo);
    }
	
	
	
	@ApiOperation(
    		value   = "패턴 대기 데이터 Total Cnt" 
    		, notes = "패턴 대기 데이터 Total Cnt를 반환 합니다.")
    @GetMapping(value = "/data/total-cnt")
    public CategoryWaitingVO selectPatternWaitingDataTotalCnt( 
    		
    		SearchVo SearchVo
    		
    		,  @ApiParam( 
					name          = "apwlSeq" 
					, type        = "int"
					, value       = "패턴 대기 리스트 일련번호"
					, example     = "0"
					, required    = false)
			@RequestParam(value = "apwlSeq", defaultValue = "0", required = false) 
			@Nullable int apwlSeq 
			
			, @ApiParam( 
					name          = "apwdIcState" 
					, type        = "String"
					, value       = "카테고리 수정 여부 - Y : 수정 후   , N : 수정 전"
					, example     = "Y"
					, required    = false)
			@RequestParam(value = "apwdIcState", defaultValue = "", required = false) 
			@Nullable String apwdIcState 
			
			, @ApiParam( 
					name          = "apwdPatState" 
					, type        = "String"
					, value       = "업로드 처리 상태  - Y : 업로드 완료 , N : 업로드 대기중"
					, example     = "Y"
					, required    = false)
			@RequestParam(value = "apwdPatState", defaultValue = "", required = false) 
			@Nullable String apwdPatState 
			
			, @ApiParam( 
					name          = "icCode" 
					, type        = "String"
					, value       = "카테고리 코드"
					, example     = ""
					, required    = false)
			@RequestParam(value = "icCode", defaultValue = "", required = false) 
			@Nullable String icCode 
			
			, @ApiParam( 
					name          = "useYn" 
					, type        = "String"
					, value       = "카테고리 사용여부"
					, example     = ""
					, required    = false)
			@RequestParam(value = "useYn", defaultValue = "", required = false) 
			@Nullable String useYn 
			) {
		
		CategoryWaitingVO vo = CategoryWaitingVO.builder()
												.apwlSeq(apwlSeq)
												.icCode(icCode)
												.apwdIcState(apwdIcState)
												.apwdPatState(apwdPatState)
												.useYn(useYn)
												.build();
		vo.setSearchVo(SearchVo);
		return service.selectPatternWaitingDataTotalCnt(vo);
    }
	
	
	
	@ApiOperation(
    		value   = "패턴 대기 데이터의 카테고리 반환" 
    		, notes = "패턴 대기 데이터 카테고리 그룹을 반환 합니다.")
    @GetMapping(value = "/data/category-group")
    public List<CategoryWaitingVO> selectPatternWaitingDataGroupByIcCode( 
    		
    		@ApiParam( 
					name          = "apwlSeq" 
					, type        = "int"
					, value       = "패턴 대기 리스트 일련번호"
					, example     = "0"
					, required    = false)
			@RequestParam(value = "apwlSeq", defaultValue = "0", required = false) 
			@Nullable int apwlSeq 
			
			, @ApiParam( 
					name          = "apwdIcState" 
					, type        = "String"
					, value       = "카테고리 수정 여부 - Y : 수정 후   , N : 수정 전"
					, example     = "Y"
					, required    = false)
			@RequestParam(value = "apwdIcState", defaultValue = "", required = false) 
			@Nullable String apwdIcState 
			
			) {
		
		CategoryWaitingVO vo = CategoryWaitingVO.builder()
								.apwlSeq(apwlSeq)
								.apwdIcState(apwdIcState)
								.build();
		
		return service.selectPatternWaitingDataGroupByIcCode(vo);
    }
	
	
	
	@ApiOperation(
    		value   = "패턴 업로드 샘플"
    		, notes = "패턴 업로드 샘플 파일을 다운로드합니다.")
    @GetMapping(value = "/pattern-upload/sample")
    public ModelAndView excelDownloadByPatternSample( ) {
    	
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("pattern.upload.sample");
		
		return modelAndView;
    }
	
	@ApiOperation(
    		value   = "패턴 업로드"
    		, notes = "패턴 파일을 업로드합니다.")
    @PostMapping(value = "/pattern-upload")
    public String excelUploadByPattern( 
    		@ApiParam( 
	    		name       = "file"
	            , type     = "MultipartFile"
	            , value    = "파일"
	            , example  = ""
	            , required = true ) 
    		@RequestParam(value = "file") MultipartFile file
    		
    		, HttpServletRequest request ) {

		return excelService.fileProcessingByPatternWaiting(file, request);
		
    }
	
	
	
	@ApiOperation(
    		value   = "패턴 대기 데이터를 수정 합니다" 
    		, notes = "패턴 대기 데이터를 수정 합니다.")
    @PatchMapping(value = "/data")
    public int updatePatternWaitingData( SearchVo SearchVo ) {
		CategoryWaitingVO vo = CategoryWaitingVO.builder().build();
		vo.setSearchVo(SearchVo);
		return service.updatePatternWaitingData(vo);
    }
	
	
	
	@ApiOperation(
    		value   = "패턴 대기 리스트를 수정 합니다" 
    		, notes = "패턴 대기 리스트를 수정 합니다.")
    @PatchMapping(value = "/list")
    public int updatePatternWaitingList( SearchVo SearchVo ) {
		CategoryWaitingVO vo = CategoryWaitingVO.builder().build();
		vo.setSearchVo(SearchVo);
		return service.updatePatternWaitingList(vo);
    }
}
