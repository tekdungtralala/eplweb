package com.wiwit.eplweb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wiwit.eplweb.dao.MatchdayVotingDAO;
import com.wiwit.eplweb.model.MatchdayVoting;
import com.wiwit.eplweb.model.User;

@Component
public class MatchdayVotingService {

	@Autowired
	private MatchdayVotingDAO matchdayVotingDAO;
	
	public List<MatchdayVoting> findByMatchdayIdsAndUser(List<Integer> ids, User user) {
		return matchdayVotingDAO.findByMatchdayIdsAndUser(ids, user);
	}
}
