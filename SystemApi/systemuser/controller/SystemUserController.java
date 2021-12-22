package com.rsn.POMS.api.poms.systemuser.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rsn.POMS.api.poms.systemuser.service.SystemUserService;
import com.rsn.POMS.api.poms.systemuser.vo.SystemUserVO;
import com.rsn.POMS.api.setting.common.vo.SearchVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @create @author 정수빈 @Date 2021 12 
 */

@RestController
@Api(tags = {"Api System User"})
@RequestMapping("/api/api-system/users")
public class SystemUserController {

	
	@Autowired
	SystemUserService service;
	
	
    @ApiOperation(
    		value   = "Api System List - User"
    		, notes = "Api System List - User")
	@GetMapping(value = "")
	public List<SystemUserVO> selectUserApiSystemList( SearchVo searchVo ) {
    	
    	SystemUserVO vo = SystemUserVO.builder().build();
    	vo.setSearchVo(searchVo);
    	
		return service.selectUserApiSystemList(vo);
	}
	 
	 
	 
	@ApiOperation(
    		value   = "Api System POMS 사용자 등록"
    		, notes = "Api System POMS 사용자 등록 진행 합니다.")
	@PostMapping(value = "")
	public SystemUserVO insertApiSystemUser(
			
			@ApiParam( name       = "puSeq"
					   , type     = "int"
					   , value    = "POMS 사용자 일련번호"
					   , example  = "0"
					   , required = true)
			 @RequestParam(value = "puSeq", defaultValue = "0", required = true)
			 @Nullable int puSeq 
			
			, @ApiParam( name      = "systemKey"
						, type     = "int"
						, value    = "API SYSTEM KEY"
						, example  = "0"
					    , required = true)
			@RequestParam(value = "systemKey", defaultValue = "0", required = true)
			@Nullable int systemKey  ) {
    	
    	SystemUserVO vo = SystemUserVO.builder()
				    			.puSeq(puSeq)
				    			.systemKey(systemKey)
				    			.build(); 
    	
		return service.insertApiSystemUser(vo);
	}
    

    
    @ApiOperation(
    		value   = "Api System 사용자 삭제"
    		, notes = "Api System 사용자를 삭제 합니다.")
	@DeleteMapping(value = "")
	public SystemUserVO deleteApiSystemUser(
			
			@ApiParam( name       = "puSeq"
					   , type     = "int"
					   , value    = "POMS 사용자 일련번호"
					   , example  = "0"
					   , required = true)
			 @RequestParam(value = "puSeq", defaultValue = "0") 
			int puSeq 
			
			, @ApiParam( name      = "systemKey"
						, type     = "int"
						, value    = "API SYSTEM KEY"
						, example  = "0"
					    , required = true)
			@RequestParam(value = "systemKey", defaultValue = "0") 
			int systemKey ){
    	
    	SystemUserVO vo = SystemUserVO.builder()
				    			.puSeq(puSeq)
				    			.systemKey(systemKey)
				    			.build(); 
    	
		return service.deleteApiSystemUser(vo);
	}

	    
}
