package com.wiwit.eplweb.dao;

import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wiwit.eplweb.model.Matchday;
import com.wiwit.eplweb.model.MatchdayVoting;
import com.wiwit.eplweb.model.User;

@Service
public class MatchdayVotingDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Transactional
	public MatchdayVoting findByUserAndMatchday(User user, Matchday match) {
		Session session = this.sessionFactory.openSession();
		List<MatchdayVoting> result = session.createQuery(
				"from MatchdayVoting where user.id=" + user.getId()
						+ " and matchday.id='" + match.getId() + "' ").list();
		session.close();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
	
	@Transactional
	public List<MatchdayVoting> findByMatchdayIdsAndUser(List<Integer> ids, User user) {
		Session session = this.sessionFactory.openSession();

		List<MatchdayVoting> result = 
				session.createQuery("FROM MatchdayVoting WHERE user.id = :userId AND matchday.id IN (:ids)")
				.setParameterList("ids", ids)
				.setParameter("userId", user.getId())
				.list();

		session.close();
		return result;
	}
}
