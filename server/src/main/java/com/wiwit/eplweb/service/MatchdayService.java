package com.wiwit.eplweb.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wiwit.eplweb.dao.MatchdayDAO;
import com.wiwit.eplweb.dao.MatchdayRatingDAO;
import com.wiwit.eplweb.dao.MatchdayVotingDAO;
import com.wiwit.eplweb.dao.PhaseDAO;
import com.wiwit.eplweb.dao.TeamDAO;
import com.wiwit.eplweb.dao.WeekDAO;
import com.wiwit.eplweb.model.Matchday;
import com.wiwit.eplweb.model.MatchdayRating;
import com.wiwit.eplweb.model.MatchdayVoting;
import com.wiwit.eplweb.model.Phase;
import com.wiwit.eplweb.model.Team;
import com.wiwit.eplweb.model.User;
import com.wiwit.eplweb.model.Week;
import com.wiwit.eplweb.model.input.MatchdayModelInput;
import com.wiwit.eplweb.model.input.ScoreModelInput;
import com.wiwit.eplweb.model.view.MatchdayModelView;
import com.wiwit.eplweb.util.VoteType;

@Component
public class MatchdayService {

	@Autowired
	private MatchdayDAO matchdayDAO;

	@Autowired
	private PhaseDAO phaseDAO;

	@Autowired
	private WeekDAO weekDAO;

	@Autowired
	private TeamDAO teamDAO;

	@Autowired
	private MatchdayRatingDAO matchdayRatingDAO;
	
	@Autowired
	private MatchdayVotingDAO matchdayVotingDAO;

	public List<Matchday> findClosestMatch(int teamId) {
		Phase p = phaseDAO.findCurrentMatchday();
		int weekNumber = Integer.valueOf(p.getValue());
		return matchdayDAO.findClosestMatch(teamId, weekNumber, 7);
	}

	public MatchdayModelView findMatchtdayOnCurrWeek() {
		String currentWeek = phaseDAO.findCurrentMatchday().getValue();
		return findMatchtdayByWeekNmr(Integer.valueOf(currentWeek));
	}
	
	public List<Matchday> findMatchtdayByWeekNumber(String weekNumber) {
		return matchdayDAO.findMatchtdayByWeekNmr(Integer.valueOf(weekNumber));
	}
	
	public List<Matchday> findMatchtdayByWeekNumber(int weekNumber) {
		return matchdayDAO.findMatchtdayByWeekNmr(Integer.valueOf(weekNumber));
	}

	public MatchdayModelView findMatchtdayByWeekNmr(int weekNumber) {
		Week week = weekDAO.findByWeekNmr(weekNumber);

		List<Matchday> listMatchday = findMatchtdayByWeekNumber(weekNumber);
		return new MatchdayModelView(listMatchday, week);
	}

	public void updateScore(int matchdayId, ScoreModelInput us) {
		Matchday m = findMatchtdayById(matchdayId);
		m.setAwayGoal(us.getAwayGoal());
		m.setHomeGoal(us.getHomeGoal());
		matchdayDAO.updateMatchday(m);
	}

	public Matchday findMatchtdayById(int matchdayId) {
		return matchdayDAO.findMatchtdayById(matchdayId);
	}
	
	public void updateVoting(Matchday matchday, User user, VoteType vote) {
		// Find latest voting
		MatchdayVoting mv = matchdayVotingDAO.findByUserAndMatchday(user, matchday);
		
		boolean newVotingData = true;
		if (mv == null || mv.getId() == 0) {
			// It is a new voting
			mv = new MatchdayVoting();
			mv.setUser(user);
			mv.setMatchday(matchday);
		} else {
			// It is not a new one

			// Set flag = false
			newVotingData = false;
			
			// Get old vote
			VoteType oldVote = VoteType.getVote(mv.getVote());
			int oldVoteValue;
			// Reduce the old vote
			switch (oldVote) {
				case HOME:
					oldVoteValue = matchday.getVotingHomeWin() -1;
					oldVoteValue = oldVoteValue < 0 ? 0 : oldVoteValue;
					matchday.setVotingHomeWin(oldVoteValue);
					break;
				case AWAY:
					oldVoteValue = matchday.getVotingAwayWin() -1;
					oldVoteValue = oldVoteValue < 0 ? 0 : oldVoteValue;
					matchday.setVotingAwayWin(oldVoteValue);
					break;
				case TIE:
					oldVoteValue = matchday.getVotingTie() -1;
					oldVoteValue = oldVoteValue < 0 ? 0 : oldVoteValue;
					matchday.setVotingTie(oldVoteValue);
					break;
			}
		}
		
		int newVoteValue;
		// Increase the new vote
		switch (vote) {
			case HOME:
				newVoteValue = matchday.getVotingHomeWin() +1;
				matchday.setVotingHomeWin(newVoteValue);
				break;
			case AWAY:
				newVoteValue = matchday.getVotingAwayWin() +1;
				matchday.setVotingAwayWin(newVoteValue);
				break;
			case TIE:
				newVoteValue = matchday.getVotingTie() + 1;
				matchday.setVotingTie(newVoteValue);
				break;
		}
		
		// Set vote on matchday
		mv.setVote(vote.getValue());		
		
		// Save matchday
		matchdayDAO.updateVoting(matchday, mv, newVotingData);
	}

	public void updateRating(Matchday matchday, User user, int ratingValue) {
		// Find latest rating
		MatchdayRating mr = matchdayRatingDAO.findByUserAndMatchday(user, matchday);
		int totalRating = matchday.getTotalRating();

		Float newRating = null;
		float currentRating = matchday.getRatingPoint();
		
		boolean newRatingData = true;
		if (mr == null) {
			// It is a new rating
			totalRating++;
			
			// Calculate 
			newRating = ((currentRating * (totalRating - 1)) + ratingValue) / totalRating;
			matchday.setTotalRating(totalRating);
			
			mr = new MatchdayRating();
			mr.setUser(user);
			mr.setMatchday(matchday);
		} else {
			// It is not a new rating
			if (totalRating == 0) {
				totalRating = 1;
				matchday.setTotalRating(1);
			}
			// Calculate 
			newRating = ((currentRating * totalRating) - mr.getRatingValue() + ratingValue) / totalRating;
			newRatingData = false;
		}
		
		// Set rating value
		mr.setRatingValue(ratingValue);
		matchday.setRatingPoint(newRating);
		
		// Save matchday
		matchdayDAO.updateRating(matchday, mr, newRatingData);

	}

	public void updateMatchdays(int weekNumber, List<MatchdayModelInput> matchs) {
		// Find all team
		List<Team> teams = teamDAO.findAll();
		// Find week by weeknumber
		Week week = weekDAO.findByWeekNmr(weekNumber);

		// Initialize new matchday list
		List<Matchday> matchdays = new ArrayList<Matchday>();

		// Iterate all matchday model
		for (MatchdayModelInput um : matchs) {

			// Instance new matchday
			Matchday m = new Matchday();

			// Set up matchday
			m.setAwayTeam(findTeamById(teams, um.getAwayTeamId()));
			m.setHomeTeam(findTeamById(teams, um.getHomeTeamId()));
			m.setTime(um.getTime());
			m.setDate(um.getDate());
			m.setWeek(week);
			m.setAwayPoint(0);
			m.setHomePoint(0);
			
			float zeroVal = Float.valueOf("0");
			m.setRatingPoint(zeroVal);
			m.setTotalRating(0);
			m.setVotingHomeWin(0);
			m.setVotingAwayWin(0);
			m.setVotingTie(0);

			// Add to the list
			matchdays.add(m);
		}

		// Create new matchdays on a week
		matchdayDAO.saveMoreMatchday(weekNumber, matchdays);
	}

	// Find team by team id on team list
	protected Team findTeamById(List<Team> teams, int teamId) {
		for (Team t : teams) {
			if (t.getId() == teamId)
				return t;
		}
		return null;
	}
}