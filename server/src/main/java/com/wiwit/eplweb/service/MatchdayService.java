package com.wiwit.eplweb.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wiwit.eplweb.dao.MatchdayDAO;
import com.wiwit.eplweb.dao.MatchdayRatingDAO;
import com.wiwit.eplweb.dao.PhaseDAO;
import com.wiwit.eplweb.dao.TeamDAO;
import com.wiwit.eplweb.dao.WeekDAO;
import com.wiwit.eplweb.model.Matchday;
import com.wiwit.eplweb.model.MatchdayRating;
import com.wiwit.eplweb.model.Phase;
import com.wiwit.eplweb.model.Team;
import com.wiwit.eplweb.model.User;
import com.wiwit.eplweb.model.Week;
import com.wiwit.eplweb.model.input.MatchdayModelInput;
import com.wiwit.eplweb.model.input.ScoreModelInput;
import com.wiwit.eplweb.model.view.MatchdayModelView;

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

	public List<Matchday> findClosestMatch(int teamId) {
		Phase p = phaseDAO.findCurrentMatchday();
		int weekNumber = Integer.valueOf(p.getValue());
		return matchdayDAO.findClosestMatch(teamId, weekNumber, 7);
	}

	public MatchdayModelView findMatchtdayOnCurrWeek() {
		String currentWeek = phaseDAO.findCurrentMatchday().getValue();
		return findMatchtdayByWeekNmr(Integer.valueOf(currentWeek));
	}

	public MatchdayModelView findMatchtdayByWeekNmr(int weekNumber) {
		Week week = weekDAO.findByWeekNmr(weekNumber);

		List<Matchday> listMatchday = matchdayDAO
				.findMatchtdayByWeekNmr(Integer.valueOf(weekNumber));
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

	public void updateRating(Matchday matchday, User user, int ratingValue) {
		MatchdayRating mr = matchdayRatingDAO.findByUserAndMatchday(user, matchday);
		int totalRating = matchday.getTotalRating();
		Float newRating = null;
		float currentRating = matchday.getRatingPoint();
		
		boolean newRatingData = true;
		if (mr == null) {
			totalRating++;
			
			newRating = ((currentRating * (totalRating - 1)) + ratingValue) / totalRating;
			matchday.setTotalRating(totalRating);
			
			mr = new MatchdayRating();
			mr.setUser(user);
			mr.setMatchday(matchday);
		} else {
			if (totalRating == 0) {
				totalRating = 1;
				matchday.setTotalRating(1);
			}
			newRating = ((currentRating * totalRating) - mr.getRatingValue() + ratingValue) / totalRating;
			newRatingData = false;
		}
		
		mr.setRatingValue(ratingValue);
		matchday.setRatingPoint(newRating);
		
		matchdayDAO.updateRating(matchday, mr, newRatingData);

	}

	public void updateMatchdays(int weekNumber, List<MatchdayModelInput> matchs) {
		List<Team> teams = teamDAO.findAll();
		Week week = weekDAO.findByWeekNmr(weekNumber);

		List<Matchday> matchdays = new ArrayList<Matchday>();

		for (MatchdayModelInput um : matchs) {
			Matchday m = new Matchday();

			m.setAwayTeam(findTeamById(teams, um.getAwayTeamId()));
			m.setHomeTeam(findTeamById(teams, um.getHomeTeamId()));
			m.setTime(um.getTime());
			m.setDate(um.getDate());
			m.setWeek(week);
			m.setAwayPoint(0);
			m.setHomePoint(0);

			matchdays.add(m);
		}

		matchdayDAO.saveMoreMatchday(weekNumber, matchdays);
	}

	protected Team findTeamById(List<Team> teams, int teamId) {
		for (Team t : teams) {
			if (t.getId() == teamId)
				return t;
		}
		return null;
	}
}