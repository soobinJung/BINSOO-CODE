package com.rsn.POMS.api.poms.systempattern.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rsn.POMS.api.poms.systempattern.dao.SystemPatternDao;
import com.rsn.POMS.api.poms.systempattern.vo.SystemPatternVO;

/**
 * @create @author 정수빈 @Date 2021.12
 */

@Service
public class SystemPatternService {

	@Autowired
	SystemPatternDao dao;
	
	/** Api System Pattern List **/
	public List<SystemPatternVO> selectApiSystemPatternList( SystemPatternVO vo ) {
		return dao.selectApiSystemPatternList(vo);
	}
	
	/** Api System Pattern TotalCnt **/
	public SystemPatternVO selectApiSystemTotalCount( SystemPatternVO vo ) {
		return dao.selectApiSystemTotalCount(vo);
	}
	
	/** Api System Pattern Find Seq **/
	public SystemPatternVO selectApiSystemPatternFindSeq( SystemPatternVO vo ) {
		return dao.selectApiSystemPatternFindSeq(vo);
	}
	
	/** Api System Pattern Insert **/
	public SystemPatternVO insertApiSystemPattern( SystemPatternVO vo ) {
		return dao.insertApiSystemPattern(vo);
	}
	
	/** Api System Pattern Update **/
	public SystemPatternVO updateApiSystemPattern( SystemPatternVO vo ) {
		return dao.updateApiSystemPattern(vo);
	}
	
	/** Api System Pattern Delete **/
	public int deleteApiSystemPattern( SystemPatternVO vo ) {
		return dao.deleteApiSystemPattern(vo);
	}
}
