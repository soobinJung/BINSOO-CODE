package com.rsn.POMS.api.dictionary.advanced.categorywaiting.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.rsn.POMS.api.dictionary.advanced.categorywaiting.vo.CategoryWaitingVO;
import com.rsn.POMS.db.ConfigPath;

@Repository("com.rsn.POMS.api.dictionary.advanced.categorywaiting")
public class CategoryWaitingDao {

	@Autowired @Qualifier("pomsSqlSession")
    private SqlSession pomsSqlSession;
	
	private static final String NAME_SPACE_LIST = "poms.advanced.category.waiting.list.";
	private static final String NAME_SPACE_DATA = "poms.advanced.category.waiting.data.";
	
	private static String NAME_TABLE = "";
	
	@Autowired
	public CategoryWaitingDao() {
		this.NAME_TABLE =  new ConfigPath().getNAME_TABLE();
	}	
	
	
	/*
	 * Pattern Waiting Data List  apwlSeq
	 */
	public List<CategoryWaitingVO> selectPatternWaitingList( CategoryWaitingVO vo ){
		vo.setDB_TABLE_NAME(NAME_TABLE);
		return pomsSqlSession.selectList(NAME_SPACE_LIST + "selectPatternWaitingList", vo);
	}
	
	public CategoryWaitingVO selectPatternWaitingListTotalCnt( CategoryWaitingVO vo ){
		vo.setDB_TABLE_NAME(NAME_TABLE);
		return pomsSqlSession.selectOne(NAME_SPACE_LIST + "selectPatternWaitingListTotalCnt", vo);
	}

	
	public int updatePatternWaitingList( CategoryWaitingVO vo ){
		vo.setDB_TABLE_NAME(NAME_TABLE);
		return pomsSqlSession.update(NAME_SPACE_LIST + "updatePatternWaitingList", vo);
	}
	
	
	/*
	 * Pattern Waiting Data
	 */
	public List<CategoryWaitingVO> selectPatternWaitingData( CategoryWaitingVO vo ){
		vo.setDB_TABLE_NAME(NAME_TABLE);
		return pomsSqlSession.selectList(NAME_SPACE_DATA + "selectPatternWaitingData", vo);
	}
	
	public CategoryWaitingVO selectPatternWaitingDataTotalCnt( CategoryWaitingVO vo ){
		vo.setDB_TABLE_NAME(NAME_TABLE);
		return pomsSqlSession.selectOne(NAME_SPACE_DATA + "selectPatternWaitingDataTotalCnt", vo);
	}
	
	public List<CategoryWaitingVO> selectPatternWaitingDataGroupByIcCode( CategoryWaitingVO vo ){
		vo.setDB_TABLE_NAME(NAME_TABLE);
		return pomsSqlSession.selectList(NAME_SPACE_DATA + "selectPatternWaitingDataGroupByIcCode", vo);
	}
	
	public int insertPatternWaitingDataList ( CategoryWaitingVO vo ) {
		vo.setDB_TABLE_NAME(NAME_TABLE);
		return pomsSqlSession.insert(NAME_SPACE_DATA + "insertPatternWaitingDataList", vo);
	}

	
	public int updatePatternWaitingData ( CategoryWaitingVO vo ) {
		vo.setDB_TABLE_NAME(NAME_TABLE);
		return pomsSqlSession.update(NAME_SPACE_DATA + "updatePatternWaitingData", vo);
	}
	
	
	public CategoryWaitingVO insertPatternWaitingList ( CategoryWaitingVO vo ) {
		vo.setDB_TABLE_NAME(NAME_TABLE);
		pomsSqlSession.insert(NAME_SPACE_LIST + "insertPatternWaitingList", vo);
		return vo;
	}
	
}
