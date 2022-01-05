package com.rsn.POMS.api.dictionary.advanced.categorywaiting.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rsn.POMS.api.dictionary.advanced.categorywaiting.dao.CategoryWaitingDao;
import com.rsn.POMS.api.dictionary.advanced.categorywaiting.vo.CategoryWaitingVO;

@Service
public class CategoryWaitingService {

	@Autowired
	private CategoryWaitingDao dao;
	
	/*
	 * Pattern Waiting Data List
	 */
	public List<CategoryWaitingVO> selectPatternWaitingList( CategoryWaitingVO vo ){
		return dao.selectPatternWaitingList(vo);
	}
	
	public CategoryWaitingVO selectPatternWaitingListTotalCnt( CategoryWaitingVO vo ){
		return dao.selectPatternWaitingListTotalCnt(vo);
	}
	
	public int updatePatternWaitingList( CategoryWaitingVO vo ){
		return dao.updatePatternWaitingList(vo);
	}
	
	/*
	 * Pattern Waiting Data
	 */
	public List<CategoryWaitingVO> selectPatternWaitingData( CategoryWaitingVO vo ){
		return dao.selectPatternWaitingData(vo);
	}
	
	public List<CategoryWaitingVO> selectPatternWaitingDataGroupByIcCode( CategoryWaitingVO vo ){
		return dao.selectPatternWaitingDataGroupByIcCode(vo);
	}
	
	public CategoryWaitingVO selectPatternWaitingDataTotalCnt( CategoryWaitingVO vo ){
		return dao.selectPatternWaitingDataTotalCnt(vo);
	}
	
	public int updatePatternWaitingData( CategoryWaitingVO vo ){
		return dao.updatePatternWaitingData(vo);
	}
}
