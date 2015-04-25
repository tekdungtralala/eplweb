package com.wiwit.eplweb.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.wiwit.eplweb.model.MatchdayComment;

@Service
public class MatchdayCommentDAO extends AbstractDAO{
	
	@Transactional
	public void createComment(MatchdayComment comment) {
		openSession(true);
		try {
			getSession().persist(comment);
			commit();
		} catch (Exception e) {
			roleback();
		}
		closeConnection();
	}
	
	// Find parent comment
	@Transactional
	public List<MatchdayComment> findByMatchAndUser(int matchdayId, int userId, 
			int offset, int size) {
		openSession();
		List<MatchdayComment> result = getSession()
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
		return result;
	}

	// Find parent comment
	@Transactional
	public List<MatchdayComment> findByMatchdayId(int matchdayId, int offset, int size) {
		openSession();
		List<MatchdayComment> result = getSession()
				.createQuery(
						"From MatchdayComment "
								+ "where matchday.id =:matchdayId "
								+ "AND parent is NULL "
								+ "ORDER BY points DESC, created ASC")
				.setParameter("matchdayId", matchdayId)
				.setFirstResult(offset)
				.setMaxResults(size)
				.list();

		return result;
	}

	// Find children comment
	@Transactional
	public List<MatchdayComment> findByParentId(int parentId, int offset,
			int size) {
		openSession();
		List<MatchdayComment> result = getSession()
				.createQuery(
						"From MatchdayComment "
								+ "where parent is not NULL and parent.id =:parentId "
								+ "ORDER BY points DESC, created ASC ")
				.setParameter("parentId", parentId)
				.setFirstResult(offset)
				.setMaxResults(size)
				.list();

		return result;
	}

	@Transactional
	public Long countTotalCommentByMatchdayId(int matchdayId) {
		openSession();

		Long count = ((Long) getSession()
				.createQuery("select count(*) " +
						"from MatchdayComment " +
						"where matchday.id =:matchdayId  " +
						"and parent is null")
				.setParameter("matchdayId", matchdayId)
				.iterate().next());
		return count;
	}
	
	@Transactional
	public Long countTotalCommentByParentId(int parentId) {
		openSession();
		Long count = ((Long) getSession()
				.createQuery("select count(*) " +
						"from MatchdayComment " +
						"where parent.id =:parentId ")
				.setParameter("parentId", parentId)
				.iterate().next());
		return count;
	}
	
	@Transactional
	public MatchdayComment findById(int id) {
		openSession();
		List<MatchdayComment> result = getSession().createQuery("from MatchdayComment " +
						"where id =:id ")
				.setParameter("id", id).list();
		
		if (result != null && result.size() > 0)
			return result.get(0);
		
		return null;
	}
}
