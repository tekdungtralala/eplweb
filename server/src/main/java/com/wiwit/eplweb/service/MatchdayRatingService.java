package com.wiwit.eplweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wiwit.eplweb.dao.MatchdayRatingDAO;
import com.wiwit.eplweb.model.Matchday;
import com.wiwit.eplweb.model.MatchdayRating;
import com.wiwit.eplweb.model.User;

@Component
public class MatchdayRatingService {

	@Autowired
	private MatchdayRatingDAO matchdayRatingDAO;
	
//	public boolean isMatchdayExist(User user, Matchday match) {
//		MatchdayRating mr = matchdayRatingDAO.findByUserAndMatchday(user, match);
//		return mr != null;
//	}
}
