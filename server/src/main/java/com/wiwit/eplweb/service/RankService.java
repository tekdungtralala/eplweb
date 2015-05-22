package com.wiwit.eplweb.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wiwit.eplweb.dao.MatchdayDAO;
import com.wiwit.eplweb.dao.PhaseDAO;
import com.wiwit.eplweb.dao.RankDAO;
import com.wiwit.eplweb.dao.TeamDAO;
import com.wiwit.eplweb.dao.WeekDAO;
import com.wiwit.eplweb.model.Matchday;
import com.wiwit.eplweb.model.Rank;
import com.wiwit.eplweb.model.Team;

@Component
public class RankService {

	@Autowired
	private RankDAO rankDAO;
	@Autowired
	private PhaseDAO phaseDAO;
	@Autowired
	private MatchdayDAO matchdayDAO;
	@Autowired
	private TeamDAO teamDAO;
	@Autowired
	private WeekDAO weekDAO;

	public List<Rank> getFiveHighestLastRank() {
		return findLatestRank().subList(0, 5);
	}

	public Rank findLatestTeamRank(int teamId) {
		String currentMatchday = phaseDAO.findCurrentMatchday().getValue();
		int prevWeek = Integer.valueOf(currentMatchday) - 1;

		return rankDAO.findTeamRankByWeeknumber(teamId, prevWeek);
	}

	public List<Rank> findLatestRank() {
		String currentMatchday = phaseDAO.findCurrentMatchday().getValue();

		// last rank must be on previous week
		int prevWeek = Integer.valueOf(currentMatchday) - 1;
		// TODO - check if prevWeek == 0

		return findRankByWeekNumber(prevWeek);
	}

	public List<Rank> findRankByWeekNumber(int weekNumber) {
		return this.rankDAO.findRankByWeekNumber(weekNumber);
	}

	public List<Rank> getRankByWeekNumber(String weekNumber) {
		return findRankByWeekNumber(Integer.valueOf(weekNumber));
	}

	public void updateRanking(int startingWeek, int endingWeek) {
		// Initialize
		HashMap<Integer, List<Rank>> rankMap = new HashMap<Integer, List<Rank>>();
		HashMap<Team, List<Matchday>> matchMap = new HashMap<Team, List<Matchday>>();

		// Get all team
		List<Team> allTeams = teamDAO.findAll();
		
		// Tmp prev ranks
		List<Rank> prevRanks = null;

		for (int i = startingWeek; i <= endingWeek; i++) {
			// All rank on a week
			List<Rank> ranks = this.findRankByWeekNumber(i);
			
			// All matchday on a week
			List<Matchday> matchsOnWeek = matchdayDAO.findMatchtdayByWeekNmr(i);
			
			// Update rank data base on previous rank and current match
			for (Team t : allTeams) {
				// Get all previous match
				List<Matchday> matchdayByTeam = matchMap.get(t);
				
				// If previous matchs is not exist then create it
				if (matchdayByTeam == null) {
					matchdayByTeam = new ArrayList<Matchday>();
					matchMap.put(t, matchdayByTeam);
				}
				// Add current match to list all previous match
				matchdayByTeam.add(getMatchday(matchsOnWeek, t));

				// Get current rank
				Rank rank = getRank(ranks, t);
				// Get previous rank
				Rank prevRank = new Rank();
				if (prevRanks != null)
					prevRank = getRank(prevRanks, t);

				// All current rank data obtained from previous matchs 
				for (Matchday m : matchdayByTeam) {
					boolean isHome = m.getHomeTeam().getId() == t.getId();

					int won = 0;
					int lost = 0;
					int draw = m.getHomeGoal() == m.getAwayGoal() ? 1 : 0;
					
					int goalScoed = isHome ? m.getHomeGoal() : m.getAwayGoal();
					int goalAgainst = isHome ? m.getAwayGoal() : m.getHomeGoal();

					// Determine who will lost/won or mybe already draw
					if (isHome) {
						won = m.getHomeGoal() > m.getAwayGoal() ? 1 : 0;
						lost = m.getHomeGoal() < m.getAwayGoal() ? 1 : 0;
					} else {
						won = m.getAwayGoal() > m.getHomeGoal() ? 1 : 0;
						lost = m.getAwayGoal() < m.getHomeGoal() ? 1 : 0;
					}

					// Set new value
					rank.setGamesWon(prevRank.getGamesWon() + won);
					rank.setGamesLost(prevRank.getGamesLost() + lost);
					rank.setGamesDrawn(prevRank.getGamesDrawn() + draw);
					rank.setGoalsScored(prevRank.getGoalsScored() + goalScoed);
					rank.setGoalsAgainst(prevRank.getGoalsAgainst() + goalAgainst);

					// calulcate point
					int points = (3 * rank.getGamesWon()) + rank.getGamesDrawn();
					rank.setPoints(points);
				}
			}
			
			// Put ranks on a week to the map
			rankMap.put(i, ranks);
			prevRanks = ranks;
		}
		// Save all ranks
		rankDAO.updateMoreRank(rankMap);
	}

	protected Rank getRank(List<Rank> ranks, Team t) {
		for (Rank r : ranks) {
			if (r.getTeam().getId() == t.getId())
				return r;
		}
		return null;
	}

	protected Matchday getMatchday(List<Matchday> matchs, Team t) {
		for (Matchday m : matchs) {
			if (m.getHomeTeam().getId() == t.getId() ||
				m.getAwayTeam().getId() == t.getId())
			return m;
		}
		return null;
	}
}
