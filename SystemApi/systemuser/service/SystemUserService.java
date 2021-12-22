package com.rsn.POMS.api.poms.systemuser.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rsn.POMS.api.poms.systemuser.dao.SystemUserDao;
import com.rsn.POMS.api.poms.systemuser.vo.SystemUserVO;

@Service
public class SystemUserService {

	@Autowired
	SystemUserDao dao;
	
	/**  Api System User List  **/
	public List<SystemUserVO> selectUserApiSystemList( SystemUserVO vo ) {
		return dao.selectUserApiSystemList( vo );
	}
	
	/** Api System User Insert **/
	public SystemUserVO insertApiSystemUser( SystemUserVO vo ) {
		dao.insertApiSystemUser(vo);
		return vo;
	}
	
	/** Api System User Delete **/
	public SystemUserVO deleteApiSystemUser( SystemUserVO vo ) {
		return dao.deleteApiSystemUser(vo);
	}
}
