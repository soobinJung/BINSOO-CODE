package com.rsn.patternwaiting.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.rsn.common.preferences.ManagerMybatisConncetion;
import com.rsn.patternwaiting.vo.PatternwaitingVO;

public class PatternwaitingDao {

private SqlSession sqlSession;
	
	private static final String MAPPER_PATH = "classpath*:com/rsn/common/dbsql/patternwaiting/**/*.xml";
	private static final String NAME_LOG = "patternwaiting.";
	private String DB_TABLE_NAME;
	
	public PatternwaitingDao( String DB_TABLE_NAME ) {
		
		this.DB_TABLE_NAME = DB_TABLE_NAME;
		try {
			sqlSession = ManagerMybatisConncetion.getSqlSession(
						"poms_outter"
							, MAPPER_PATH
						);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public List<PatternwaitingVO> selectAnaPatternWaitingList (PatternwaitingVO vo){
		vo.setDB_TABLE_NAME(DB_TABLE_NAME);
		return sqlSession.selectList(NAME_LOG + "selectAnaPatternWaitingList", vo);
	}
	
	
	public PatternwaitingVO updateAnaPatternWaitingList (PatternwaitingVO vo){
		vo.setDB_TABLE_NAME(DB_TABLE_NAME);
		sqlSession.update(NAME_LOG + "updateAnaPatternWaitingList", vo);
		return vo;
	}
	

	public PatternwaitingVO selectAnaPatternWaitingData (PatternwaitingVO vo){
		vo.setDB_TABLE_NAME(DB_TABLE_NAME);
		return sqlSession.selectOne(NAME_LOG + "selectAnaPatternWaitingData", vo);
	}

	
	public int updateAnaPatternWaitingData (PatternwaitingVO vo){
		vo.setDB_TABLE_NAME(DB_TABLE_NAME);
		return sqlSession.update(NAME_LOG + "updateAnaPatternWaitingData", vo);
	}
	
	
	public PatternwaitingVO insertAnaPatternWaitingData (PatternwaitingVO vo){
		vo.setDB_TABLE_NAME(DB_TABLE_NAME);
		sqlSession.insert(NAME_LOG + "insertAnaPatternWaitingData", vo);
		return vo;
	}
}
