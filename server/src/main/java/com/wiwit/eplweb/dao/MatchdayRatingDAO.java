package com.wiwit.eplweb.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.wiwit.eplweb.model.Matchday;
import com.wiwit.eplweb.model.MatchdayRating;
import com.wiwit.eplweb.model.User;

@Service
public class MatchdayRatingDAO extends AbstractDAO{

	@Transactional
	public MatchdayRating findByUserAndMatchday(User user, Matchday match) {
		openSession();
		List<MatchdayRating> result = getSession().createQuery(
				"from MatchdayRating where user.id=" + user.getId()
						+ " and matchday.id='" + match.getId() + "' ").list();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
}
