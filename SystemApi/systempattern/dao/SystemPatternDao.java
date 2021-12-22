package com.rsn.POMS.api.poms.systempattern.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.rsn.POMS.api.poms.systempattern.vo.SystemPatternVO;
import com.rsn.POMS.db.ConfigPath;

/**
 * @create @author 정수빈 @Date 2021.12
 */

@Repository("com.rsn.POMS.api.poms.systempattern")
public class SystemPatternDao {

	@Autowired @Qualifier("pomsSqlSession")
    private SqlSession pomsSqlSession;
	
	private static final String NAME_SPACE = "system.pattern.";
	private static String NAME_TABLE = "";
	
	public SystemPatternDao() {
		this.NAME_TABLE =  new ConfigPath().getNAME_TABLE();
	}
	
	/** Api System Pattern List **/
	public List<SystemPatternVO> selectApiSystemPatternList( SystemPatternVO vo ) {
		vo.setDB_TABLE_NAME(NAME_TABLE);
		return pomsSqlSession.selectList(NAME_SPACE + "selectApiSystemPatternList", vo );
	}
	
	/** Api System TotalCnt **/
	public SystemPatternVO selectApiSystemTotalCount( SystemPatternVO vo ) {
		vo.setDB_TABLE_NAME(NAME_TABLE);
		return pomsSqlSession.selectOne(NAME_SPACE + "selectApiSystemTotalCount", vo );
	}
	
	/** Api System TotalCnt **/
	public SystemPatternVO selectApiSystemPatternFindSeq( SystemPatternVO vo ) {
		vo.setDB_TABLE_NAME(NAME_TABLE);
		return pomsSqlSession.selectOne(NAME_SPACE + "selectApiSystemPatternFindSeq", vo );
	}
	
	/** Api System Insert **/
	public SystemPatternVO insertApiSystemPattern( SystemPatternVO vo ) {
		vo.setDB_TABLE_NAME(NAME_TABLE);
		pomsSqlSession.insert(NAME_SPACE + "insertApiSystemPattern", vo );
		return vo;
	}
	
	/** Api System Update **/
	public SystemPatternVO updateApiSystemPattern( SystemPatternVO vo ) {
		vo.setDB_TABLE_NAME(NAME_TABLE);
		pomsSqlSession.update(NAME_SPACE + "updateApiSystemPattern", vo );
		return vo;
	}
	
	/** Api System Delete **/
	public int deleteApiSystemPattern( SystemPatternVO vo ) {
		vo.setDB_TABLE_NAME(NAME_TABLE);
		return pomsSqlSession.update(NAME_SPACE + "updateApiSystemPattern", vo );
	}
}
