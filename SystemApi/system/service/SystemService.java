package com.rsn.POMS.api.poms.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rsn.POMS.api.poms.system.dao.SystemDao;
import com.rsn.POMS.api.poms.system.vo.SystemVo;

/**
 * @create @author 정수빈 @Date 2021 09
 */

@Service
public class SystemService {
	
	@Autowired
	SystemDao dao;
	
	/** Api System List **/
	public List<SystemVo> selectApiSystemList( SystemVo vo ) {
		return dao.selectApiSystemList(vo);
	}
	
	/** Api System TotalCnt **/
	public SystemVo selectApiSystemTotalCount( SystemVo vo ) {
		return dao.selectApiSystemTotalCount(vo);
	}
	
	
	
	/** Api System Insert **/
	public SystemVo insertApiSystem( SystemVo vo ) {
		return dao.insertApiSystem(vo);
	}
	
	
	
	/** Api System Update **/
	public int updateApiSystem( SystemVo vo ) {
		return dao.updateApiSystem(vo);
	}
	
}
