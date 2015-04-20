package com.wiwit.eplweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wiwit.eplweb.dao.CommentPointDAO;
import com.wiwit.eplweb.model.CommentPoint;

@Component
public class CommentPointService {

	@Autowired
	private CommentPointDAO commentPointDAO;
	
	public CommentPoint findByCommentIdAndUser(int commentId, int userId) {
		return commentPointDAO.findByCommentIdAndUser(commentId, userId);
	}
	
	public void updatePoint(CommentPoint cp, boolean isNew) {	
		commentPointDAO.updatePoint(cp, isNew);		
	}
}
