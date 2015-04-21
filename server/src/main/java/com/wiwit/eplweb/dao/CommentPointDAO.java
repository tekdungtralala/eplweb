package com.wiwit.eplweb.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wiwit.eplweb.model.CommentPoint;
import com.wiwit.eplweb.model.MatchdayComment;

@Service
public class CommentPointDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Transactional
	public void updatePoint(CommentPoint point, Boolean latestValue) {
		MatchdayComment comment = point.getMatchdayComment();
		int latestPoint = comment.getPoints();
		
		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
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
		
		tx.commit();
		session.close();
	}
	
	@Transactional
	public List<CommentPoint> findByMatchIdAndUserId(int matchdayId, int userId) {
		Session session = this.sessionFactory.openSession();
		List<CommentPoint> result = session
				.createQuery("from CommentPoint " +
						"where matchdayComment.matchday.id=:matchdayId " +
						"and user.id=:userId")
				.setParameter("matchdayId", matchdayId)
				.setParameter("userId", userId)
				.list();
		session.close();		
		return result;
	}
	
	@Transactional
	public CommentPoint findByCommentIdAndUser(int commentId, int userId) {
		Session session = this.sessionFactory.openSession();
		List<CommentPoint> result = session
				.createQuery("from CommentPoint where matchdayComment.id=:commentId and user.id=:userId")
				.setParameter("commentId", commentId)
				.setParameter("userId", userId)
				.list();
		session.close();
		
		if (result != null && result.size() > 0)
			return result.get(0);
		
		return null;
	}

}
