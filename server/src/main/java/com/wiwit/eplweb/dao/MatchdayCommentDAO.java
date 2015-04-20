package com.wiwit.eplweb.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wiwit.eplweb.model.MatchdayComment;

@Service
public class MatchdayCommentDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Transactional
	public void createComment(MatchdayComment comment) {
		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		session.persist(comment);
		
		tx.commit();
		session.close();
	}
	
	// Find parent comment
	@Transactional
	public List<MatchdayComment> findByMatchAndUser(int matchdayId, int userId, 
			int offset, int size) {
		Session session = this.sessionFactory.openSession();
		List<MatchdayComment> result = session
				.createQuery(
						"From MatchdayComment "
								+ "where matchday.id =:matchdayId "
								+ "AND parent is NULL "
								+ "AND user.id =:userId "
								+ "ORDER BY points DESC, created ASC")
				.setParameter("matchdayId", matchdayId)
				.setParameter("userId", userId)
				.setFirstResult(offset)
				.setMaxResults(size)
				.list();

		session.close();
		return result;
	}

	// Find parent comment
	@Transactional
	public List<MatchdayComment> findByMatchdayId(int matchdayId, int offset, int size) {
		Session session = this.sessionFactory.openSession();
		List<MatchdayComment> result = session
				.createQuery(
						"From MatchdayComment "
								+ "where matchday.id =:matchdayId "
								+ "AND parent is NULL "
								+ "ORDER BY points DESC, created ASC")
				.setParameter("matchdayId", matchdayId)
				.setFirstResult(offset)
				.setMaxResults(size)
				.list();

		session.close();
		return result;
	}

	// Find children comment
	@Transactional
	public List<MatchdayComment> findByParentId(int parentId, int offset,
			int size) {
		Session session = this.sessionFactory.openSession();
		List<MatchdayComment> result = session
				.createQuery(
						"From MatchdayComment "
								+ "where parent is not NULL and parent.id =:parentId "
								+ "ORDER BY points DESC, created ASC ")
				.setParameter("parentId", parentId)
				.setFirstResult(offset)
				.setMaxResults(size)
				.list();

		session.close();
		return result;
	}

	@Transactional
	public Long countTotalCommentByMatchdayId(int matchdayId) {
		Session session = this.sessionFactory.openSession();

		Long count = ((Long) session
				.createQuery("select count(*) " +
						"from MatchdayComment " +
						"where matchday.id =:matchdayId  " +
						"and parent is null")
				.setParameter("matchdayId", matchdayId)
				.iterate().next());
		session.close();
		return count;
	}
	
	@Transactional
	public Long countTotalCommentByParentId(int parentId) {
		Session session = this.sessionFactory.openSession();

		Long count = ((Long) session
				.createQuery("select count(*) " +
						"from MatchdayComment " +
						"where parent.id =:parentId ")
				.setParameter("parentId", parentId)
				.iterate().next());
		session.close();
		return count;
	}
	
	@Transactional
	public MatchdayComment findById(int id) {
		Session session = this.sessionFactory.openSession();

		List<MatchdayComment> result= session.createQuery("from MatchdayComment " +
						"where id =:id ")
				.setParameter("id", id).list();
		session.close();
		
		if (result != null && result.size() > 0)
			return result.get(0);
		
		return null;
	}
}
