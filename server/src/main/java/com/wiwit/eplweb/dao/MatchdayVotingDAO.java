package com.wiwit.eplweb.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.wiwit.eplweb.model.Matchday;
import com.wiwit.eplweb.model.MatchdayVoting;
import com.wiwit.eplweb.model.User;

@Service
public class MatchdayVotingDAO extends AbstractDAO{
	
	@Transactional
	public MatchdayVoting findByUserAndMatchday(User user, Matchday match) {
		openSession();
		List<MatchdayVoting> result = getSession().createQuery(
				"from MatchdayVoting where user.id=" + user.getId()
						+ " and matchday.id='" + match.getId() + "' ").list();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
	
	@Transactional
	public List<MatchdayVoting> findByMatchdayIdsAndUser(List<Integer> ids, User user) {
		openSession();

		List<MatchdayVoting> result = getSession()
				.createQuery("FROM MatchdayVoting " +
						"WHERE user.id = :userId " +
						"AND matchday.id IN (:ids)")
				.setParameterList("ids", ids)
				.setParameter("userId", user.getId())
				.list();
		return result;
	}
}
