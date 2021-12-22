package com.rsn.POMS.api.poms.system.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.rsn.POMS.api.poms.system.vo.SystemVo;
import com.rsn.POMS.db.ConfigPath;


/**
 * @create @author 정수빈 @Date 2021 09
 */

@Repository("com.rsn.POMS.api.code.system")
public class SystemDao {

	@Autowired @Qualifier("pomsSqlSession")
    private SqlSession pomsSqlSession;
	
	private static final String NAME_SPACE = "api.system.";
	private static String NAME_TABLE = "";
	
	public SystemDao() {
		this.NAME_TABLE =  new ConfigPath().getNAME_TABLE();
	}
	
	/** Api System List **/
	public List<SystemVo> selectApiSystemList( SystemVo vo ) {
		vo.setDB_TABLE_NAME(NAME_TABLE);
		return pomsSqlSession.selectList(NAME_SPACE + "selectApiSystemList", vo );
	}
	
	/** Api System TotalCnt **/
	public SystemVo selectApiSystemTotalCount( SystemVo vo ) {
		vo.setDB_TABLE_NAME(NAME_TABLE);
		return pomsSqlSession.selectOne(NAME_SPACE + "selectApiSystemTotalCount", vo );
	}
	
	
	
	/** Api System Insert **/
	public SystemVo insertApiSystem( SystemVo vo ) {
		vo.setDB_TABLE_NAME(NAME_TABLE);
		pomsSqlSession.insert(NAME_SPACE + "insertApiSystem", vo );
		return vo;
	}
	
	
	/** Api System Update **/
	public int updateApiSystem( SystemVo vo ) {
		vo.setDB_TABLE_NAME(NAME_TABLE);
		return pomsSqlSession.update(NAME_SPACE + "updateApiSystem", vo );
	}
 }