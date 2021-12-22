package com.rsn.POMS.api.poms.systemuser.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.rsn.POMS.api.poms.systemuser.vo.SystemUserVO;
import com.rsn.POMS.db.ConfigPath;

@Repository("com.rsn.POMS.api.poms.systemuser")
public class SystemUserDao {

	@Autowired @Qualifier("pomsSqlSession")
    private SqlSession pomsSqlSession;
	
	private static final String NAME_SPACE = "api.system.user.";
	private static String NAME_TABLE = "";
	
	public SystemUserDao() {
		this.NAME_TABLE =  new ConfigPath().getNAME_TABLE();
	}

	
	/** Api System User List **/
	public List<SystemUserVO> selectUserApiSystemList( SystemUserVO vo ) {
		vo.setDB_TABLE_NAME(NAME_TABLE);
		return pomsSqlSession.selectList(NAME_SPACE + "selectUserApiSystemList", vo);
	}
	
	
	/** Api System User Delete **/
	public SystemUserVO deleteApiSystemUser( SystemUserVO vo ) {
		vo.setDB_TABLE_NAME(NAME_TABLE);
		pomsSqlSession.delete(NAME_SPACE + "deleteApiSystemUser", vo );
		return vo;
	}
	
	
	/** Api System User Insert **/
	public SystemUserVO insertApiSystemUser( SystemUserVO vo ) {
		vo.setDB_TABLE_NAME(NAME_TABLE);
		pomsSqlSession.insert(NAME_SPACE + "insertApiSystemUser", vo );
		return vo;
	}
}
