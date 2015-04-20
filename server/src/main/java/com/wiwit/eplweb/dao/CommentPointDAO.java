package com.wiwit.eplweb.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wiwit.eplweb.model.CommentPoint;

@Service
public class CommentPointDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Transactional
	public void updatePoint(CommentPoint point, boolean isNew) {
		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		 
		if (isNew)
			session.persist(point);
		else
			session.update(point);
		
		tx.commit();
		session.close();
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
