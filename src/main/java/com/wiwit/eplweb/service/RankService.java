package com.wiwit.eplweb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wiwit.eplweb.dao.PhaseDAO;
import com.wiwit.eplweb.dao.RankDAO;
import com.wiwit.eplweb.model.Rank;

@Component
public class RankService {

	@Autowired
	private RankDAO rankDAO;
	
	@Autowired
	private PhaseDAO phaseDAO;

	public List<Rank> getFiveHighestLastRank() {
		return getLatestRank().subList(0, 5);
	}
	
	public List<Rank> getLatestRank() {
		String currentMatchday = phaseDAO.getCurrentMatchday().getValue();

		// last rank must be on previous week
		int prevWeek = Integer.valueOf(currentMatchday) - 1;
		// TODO - check if prevWeek == 0
		
		return getRankByWeekNumber(prevWeek);
	}	
	
	public List<Rank> getRankByWeekNumber(int weekNumber) {
		return this.rankDAO.getRankByWeekNumber(weekNumber);
	}
	
	public List<Rank> getRankByWeekNumber(String weekNumber) {
		return getRankByWeekNumber(Integer.valueOf(weekNumber));
	}	
}
