package com.wiwit.eplweb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wiwit.eplweb.dao.MatchdayCommentDAO;
import com.wiwit.eplweb.model.MatchdayComment;

@Component
public class MatchdayCommentService {

	@Autowired
	private MatchdayCommentDAO matchdayCommentDAO;

	public List<MatchdayComment> findByMathdayId(int matchdayId, int offset, int size) {
		return matchdayCommentDAO.findByMatchdayId(matchdayId, offset, size);
	}
	
	public List<MatchdayComment> findByParentId(int parentId, int offset, int size) {
		return matchdayCommentDAO.findByParentId(parentId, offset, size);
	}	
	
	public Long countTotalCommentByMatchdayId(int matchdayId) {
		return matchdayCommentDAO.countTotalCommentByMatchdayId(matchdayId);
	}
	
	public Long countTotalCommentByParentId(int parentId) {
		return matchdayCommentDAO.countTotalCommentByParentId(parentId);
	}
}
