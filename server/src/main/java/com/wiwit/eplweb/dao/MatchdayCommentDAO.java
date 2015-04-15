package com.wiwit.eplweb.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wiwit.eplweb.model.MatchdayComment;

@Service
public class MatchdayCommentDAO {

	@Autowired
	private SessionFactory sessionFactory;

	// Find parent comment
	@Transactional
	public List<MatchdayComment> findByMatchdayId(int matchdayId) {
		Session session = this.sessionFactory.openSession();
		List<MatchdayComment> result = 
				session.createQuery("From MatchdayComment " +
						"where matchday.id =:matchdayId " +
						"AND parent is NULL " +
						"ORDER BY points DESC, created ASC")
				.setParameter("matchdayId", matchdayId)
				.list();

		session.close();
		return result;
	}
	
	// Find children comment
	@Transactional
	public List<MatchdayComment> findByParentId(int parentId, int offset, int size) {
		Session session = this.sessionFactory.openSession();
		List<MatchdayComment> result = 
				session.createQuery("From MatchdayComment " +
						"where parent is not NULL and parent.id =:parentId " +
						"ORDER BY points DESC, created ASC ")
				.setParameter("parentId", parentId)
				.setFirstResult(offset)
				.setMaxResults(size)
				.list();

		session.close();
		return result;
	}	
}
