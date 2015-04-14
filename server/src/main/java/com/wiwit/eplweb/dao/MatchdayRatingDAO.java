package com.wiwit.eplweb.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wiwit.eplweb.model.Matchday;
import com.wiwit.eplweb.model.MatchdayRating;
import com.wiwit.eplweb.model.User;

@Service
public class MatchdayRatingDAO {
	@Autowired
	private SessionFactory sessionFactory;

	@Transactional
	public MatchdayRating findByUserAndMatchday(User user, Matchday match) {
		Session session = this.sessionFactory.openSession();
		List<MatchdayRating> result = session.createQuery(
				"from MatchdayRating where user.id=" + user.getId()
						+ " and matchday.id='" + match.getId() + "' ").list();
		session.close();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
}
