package com.wiwit.eplweb.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.springframework.stereotype.Service;

import com.wiwit.eplweb.model.CommentPoint;
import com.wiwit.eplweb.model.MatchdayComment;

@Service
public class CommentPointDAO extends AbstractDAO{
	
	@Transactional
	public void updatePoint(CommentPoint point, Boolean latestValue) {
		openSession(true);
		try {
			MatchdayComment comment = point.getMatchdayComment();
			int latestPoint = comment.getPoints();
			
			Session session = getSession();
			
			if (latestValue == null) {
				if (point.getIsUp()) {
					latestPoint++;
				} else {
					latestPoint--;
				}
				session.persist(point);
			} else {
				if (latestValue && !point.getIsUp()) {
					latestPoint--;
				} else if (!latestValue && point.getIsUp()) {
					latestPoint++;
				}
				session.update(point);
			}
			
			if (latestPoint < 0) latestPoint = 0;
			comment.setPoints(latestPoint);
			session.update(comment); 
			
			commit();
		} catch (Exception e) {
			roleback();
		}
		closeConnection();
	}
	
	@Transactional
	public List<CommentPoint> findByMatchIdAndUserId(int matchdayId, int userId) {
		openSession();
		List<CommentPoint> result = getSession()
				.createQuery("from CommentPoint " +
						"where matchdayComment.matchday.id=:matchdayId " +
						"and user.id=:userId")
				.setParameter("matchdayId", matchdayId)
				.setParameter("userId", userId)
				.list();
		return result;
	}
	
	@Transactional
	public CommentPoint findByCommentIdAndUser(int commentId, int userId) {
		openSession();
		List<CommentPoint> result = getSession()
				.createQuery("from CommentPoint where matchdayComment.id=:commentId and user.id=:userId")
				.setParameter("commentId", commentId)
				.setParameter("userId", userId)
				.list();
		
		if (result != null && result.size() > 0)
			return result.get(0);
		
		return null;
	}

}
