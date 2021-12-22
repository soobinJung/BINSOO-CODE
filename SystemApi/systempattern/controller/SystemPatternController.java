package com.rsn.POMS.api.poms.systempattern.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rsn.POMS.api.poms.systempattern.service.SystemPatternService;
import com.rsn.POMS.api.poms.systempattern.vo.SystemPatternVO;
import com.rsn.POMS.api.setting.common.vo.SearchVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @create @author 정수빈 @Date 2021.12
 */

@RestController
@Api(tags = {"ApiSystemPattern"})
@RequestMapping("/api/system/pattern")
public class SystemPatternController {


	@Autowired
	SystemPatternService service;
	
	@ApiOperation(
			value   = "Api System Pattern List"
			, notes = "Api System Pattern List.")
	@GetMapping(value = "")
	public List<SystemPatternVO> selectApiSystemPatternList( 
			
			SearchVo searchVo
			
			, @ApiParam( 
					name          = "useYn" 
					, type        = "String"
					, value       = "사용 유무"
					, example     = "Y"
					, required    = true)
			@RequestParam(value = "useYn", defaultValue = "Y", required = true) 
			@Nullable String useYn
			
			, @ApiParam( 
					name          = "systemKey" 
					, type        = "int"
					, value       = "API_SYSTEM 일련번호"
					, example     = "0"
					, required    = true)
			@RequestParam(value = "systemKey", defaultValue = "0", required = true) 
			@Nullable int systemKey
		){
	
		SystemPatternVO vo = SystemPatternVO.builder()
											.useYn(useYn)
											.systemKey(systemKey)
											.build();
		vo.setSearchVo(searchVo);
		
		return service.selectApiSystemPatternList(vo);
	}
	
	
	
	@ApiOperation(
			value   = "Api System Pattern Total Cnt"
			, notes = "Api System Pattern Total Cnt.")
	@GetMapping(value = "/total-cnt")
	public SystemPatternVO selectApiSystemTotalCount( 
			
			SearchVo searchVo
			
			, @ApiParam( 
					name          = "useYn" 
					, type        = "String"
					, value       = "사용 유무"
					, example     = "Y"
					, required    = true)
			@RequestParam(value = "useYn", defaultValue = "Y", required = true) 
			@Nullable String useYn
			
			, @ApiParam( 
					name          = "systemKey" 
					, type        = "int"
					, value       = "API_SYSTEM 일련번호"
					, example     = "0"
					, required    = true)
			@RequestParam(value = "systemKey", defaultValue = "0", required = true) 
			@Nullable int systemKey 
		){
	
		SystemPatternVO vo = SystemPatternVO.builder()
											.useYn(useYn)
											.systemKey(systemKey)
											.build();
		vo.setSearchVo(searchVo);
		
		return service.selectApiSystemTotalCount(vo);
	}
	
	
	@ApiOperation(
			value   = "Api System Pattern Find One By Seq"
			, notes = "Api System Pattern Find One By Seq.")
	@GetMapping(value = "/{seq}")
	public SystemPatternVO selectApiSystemPatternFindSeq( @PathVariable(name = "seq", required = true) int seq ){
	
		SystemPatternVO vo = SystemPatternVO.builder()
								.systemPatternSeq(seq)
								.build();
		
		return service.selectApiSystemPatternFindSeq(vo);
	}
	
	
	@ApiOperation(
			value   = "Api System Pattern Insert"
			, notes = "Api System Pattern Insert.")
	@PostMapping(value = "")
	public SystemPatternVO insertApiSystemPattern( 
			
			@ApiParam( 
					name          = "systemKey" 
					, type        = "int"
					, value       = "API_SYSTEM 일련번호"
					, example     = "1"
					, required    = true)
			@RequestParam(value = "systemKey", defaultValue = "0", required = true) 
			@Nullable int systemKey 
			
			, @ApiParam( 
					name          = "icSeq" 
					, type        = "int"
					, value       = "ANA_ISSUE_CODE 일련번호"
					, example     = "1"
					, required    = true)
			@RequestParam(value = "icSeq", defaultValue = "0", required = true) 
			@Nullable int icSeq 
			
			, @ApiParam( 
					name          = "icType" 
					, type        = "int"
					, value       = "ANA_ISSUE_CODE 분류 타입"
					, example     = "1"
					, required    = true)
			@RequestParam(value = "icType", defaultValue = "0", required = true) 
			@Nullable int icType 
			
			, @ApiParam( 
					name          = "useYn" 
					, type        = "String"
					, value       = "사용 유무"
					, example     = "Y"
					, required    = true)
			@RequestParam(value = "useYn", defaultValue = "Y", required = true) 
			@Nullable String useYn
		){
	
		SystemPatternVO vo = SystemPatternVO.builder()
							.systemKey(systemKey)
							.icSeq(icSeq)
							.icType(icType)
							.useYn(useYn)
							.build();
					
		return service.insertApiSystemPattern(vo);
	}
	
	
	
	@ApiOperation(
			value   = "Api System Pattern Update"
			, notes = "Api System Pattern Update.")
	@PatchMapping(value = "")
	public SystemPatternVO updateApiSystemPattern(SearchVo searchVo ){
	
		SystemPatternVO vo = SystemPatternVO.builder().build();
		vo.setSearchVo(searchVo);
		
		return service.updateApiSystemPattern(vo);
	}
	
	
	
	@ApiOperation(
			value   = "Api System Pattern Delete"
			, notes = "Api System Pattern Delete.")
	@DeleteMapping(value = "")
	public int deleteApiSystemPattern(SearchVo searchVo ){
	
		SystemPatternVO vo = SystemPatternVO.builder().build();
		vo.setSearchVo(searchVo);
		
		return service.deleteApiSystemPattern(vo);
	}
}
