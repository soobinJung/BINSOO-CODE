package com.rsn.POMS.api.poms.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rsn.POMS.api.setting.common.vo.SearchVo;
import com.rsn.POMS.api.poms.system.service.SystemService;
import com.rsn.POMS.api.poms.system.vo.SystemVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @create @author 정수빈 @Date 2021 11 05
 * @update @author 정수빈 @Date 2021 11
 */

@RestController
@Api(tags = {"Api System"})
@RequestMapping("/api/api-system")
public class SystemController {
	
	@Autowired
	SystemService service;
	

    @ApiOperation(
    		value   = "Api System 조회"
    		, notes = "Api System 조회 한다.")
	@GetMapping(value = "")
	public List<SystemVo> selectApiSystemList(
			SearchVo searchVo
			
			, @ApiParam( 
					name          = "autoAnal" 
					, type        = "String"
					, value       = "자동분석 여부 - Y:ON, N:OFF"
					, example     = "Y"
					, required    = false)
			@RequestParam(value = "autoAnal", defaultValue = "", required = false) 
			@Nullable String autoAnal 
			
			, @ApiParam( 
					name          = "systemKey" 
					, type        = "int"
					, value       = "시스템 일련번호"
					, example     = "0"
					, required    = false)
			@RequestParam(value = "systemKey", defaultValue = "0", required = false) 
			@Nullable int systemKey 
			
			) {
    	SystemVo vo = SystemVo.builder()
    							.autoAnal(autoAnal)
    							.systemKey(systemKey)
    							.build();
    	vo.setSearchVo(searchVo);
		return service.selectApiSystemList(vo);
	}
	
    
    
    @ApiOperation(
    		value   = "Api System Total Count"
    		, notes = "Api System 전체 개수를 조회한다.")
	@GetMapping(value = "/total-cnt")
	public SystemVo selectApiSystemTotalCount( 
			SearchVo searchVo
			
			, @ApiParam( 
					name          = "autoAnal" 
					, type        = "String"
					, value       = "자동분석 여부 - Y:ON, N:OFF"
					, example     = "Y"
					, required    = false)
			@RequestParam(value = "autoAnal", defaultValue = "", required = false) 
			@Nullable String autoAnal  ) {
    	SystemVo vo = SystemVo.builder().autoAnal(autoAnal).build();
    	vo.setSearchVo(searchVo);
		return service.selectApiSystemTotalCount(vo);
	}
	
    
    
   
	
    
    
    
    
    
    @ApiOperation(
    		value   = "Api System 등록"
    		, notes = "Api System 등록 진행 합니다.")
	@PostMapping(value = "")
	public SystemVo insertApiSystem(
			
			@ApiParam( 
					name          = "system" 
					, type        = "String"
					, value       = "시스템명"
					, example     = ""
					, required    = true )
			@RequestParam(value = "system", defaultValue = "", required = true) 
			@Nullable String system
			
			, @ApiParam( 
					name          = "admin1" 
					, type        = "String"
					, value       = "관리자1"
					, example     = ""
					, required    = true )
			@RequestParam(value = "admin1", defaultValue = "", required = true) 
			@Nullable String admin1
			
			, @ApiParam( 
					name          = "admin2" 
					, type        = "String"
					, value       = "관리자2"
					, example     = ""
					, required    = true )
			@RequestParam(value = "admin2", defaultValue = "", required = true) 
			@Nullable String admin2		

			, @ApiParam( 
					name          = "domain" 
					, type        = "String"
					, value       = "도메인2"
					, example     = ""
					, required    = true )
			@RequestParam(value = "domain", defaultValue = "", required = true) 
			@Nullable String domain		
			
			, @ApiParam( 
					name          = "host" 
					, type        = "String"
					, value       = "호스트 주소1"
					, example     = ""
					, required    = true )
			@RequestParam(value = "host", defaultValue = "", required = true) 
			@Nullable String host		
			
			, @ApiParam( 
					name          = "host2" 
					, type        = "String"
					, value       = "호스트 주소2"
					, example     = ""
					, required    = true )
			@RequestParam(value = "host2", defaultValue = "", required = true) 
			@Nullable String host2	 
			
			, @ApiParam( 
					name          = "description" 
					, type        = "String"
					, value       = "비고"
					, example     = ""
					, required    = true )
			@RequestParam(value = "description", defaultValue = "", required = true) 
			@Nullable String description	 
			
			, @ApiParam( 
					name          = "autoAnal" 
					, type        = "String"
					, value       = "자동 분석 여부"
					, example     = "Y"
					, required    = true )
			@RequestParam(value = "autoAnal", defaultValue = "", required = true) 
			@Nullable String autoAnal		 
			
			, @ApiParam( 
					name          = "issueData" 
					, type        = "String"
					, value       = "이슈 분석 여부"
					, example     = ""
					, required    = true )
			@RequestParam(value = "issueData", defaultValue = "", required = true) 
			@Nullable String issueData		 
			
			, @ApiParam( 
					name          = "client" 
					, type        = "String"
					, value       = "client"
					, example     = ""
					, required    = true )
			@RequestParam(value = "client", defaultValue = "", required = true) 
			@Nullable String client	 
			
			, @ApiParam( 
					name          = "systemUse" 
					, type        = "String"
					, value       = "client"
					, example     = "Y"
					, required    = true )
			@RequestParam(value = "systemUse", defaultValue = "", required = true) 
			@Nullable String systemUse  ){
    	
    	SystemVo vo = SystemVo.builder()
				    			.system(system)
				    			.admin1(admin1)
				    			.admin2(admin2)
				    			.domain(domain)
				    			.host(host)
				    			.host2(host2)
				    			.description(description)
				    			.autoAnal(autoAnal)
				    			.issueData(issueData)
				    			.client(client)
				    			.systemUse(systemUse)
				    			.build();
    	
		return service.insertApiSystem(vo);
	}
    
    
	
    @ApiOperation(
    		value   = "Api System 수정"
    		, notes = "Api System 수정 진행 합니다.")
	@PatchMapping(value = "")
	public int updateApiSystem( SearchVo searchVo ) {
    	
    	SystemVo vo = SystemVo.builder().build();
    	vo.setSearchVo(searchVo);
    	
		return service.updateApiSystem(vo);
	}
}
