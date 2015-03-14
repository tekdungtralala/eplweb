package com.wiwit.eplweb.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wiwit.eplweb.dao.MatchdayDAO;
import com.wiwit.eplweb.dao.PhaseDAO;
import com.wiwit.eplweb.dao.TeamDAO;
import com.wiwit.eplweb.dao.WeekDAO;
import com.wiwit.eplweb.model.Matchday;
import com.wiwit.eplweb.model.Phase;
import com.wiwit.eplweb.model.Team;
import com.wiwit.eplweb.model.Week;
import com.wiwit.eplweb.model.input.UpdateMatchday;
import com.wiwit.eplweb.model.input.UpdateScore;
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
	
	public List<Matchday> getLastAndNextMatchday(int teamId){
		Phase p = phaseDAO.getCurrentMatchday();
		int weekNumber = Integer.valueOf(p.getValue());
		return matchdayDAO.getLastAndNextMatchday(teamId, weekNumber, 7);
	}
	
	public MatchdayModelView getMatchtdayOnCurrWeek(){
		String currentWeek = phaseDAO.getCurrentMatchday().getValue();
		return getMatchtdayByWeekNmr(Integer.valueOf(currentWeek));
	}
	
	public MatchdayModelView getMatchtdayByWeekNmr(int weekNumber){
		Week week = weekDAO.findByWeekNmr(weekNumber);
		
		List<Matchday>  listMatchday = matchdayDAO.findMatchtdayByWeekNmr(Integer.valueOf(weekNumber));
		return new MatchdayModelView(listMatchday, week);
	}
	
	public void updateScore(int matchdayId, UpdateScore us) {
		Matchday m = matchdayDAO.findMatchtdayById(matchdayId);
		m.setAwayGoal(us.getAwayGoal());
		m.setHomeGoal(us.getHomeGoal());
		matchdayDAO.updateMatchday(m);
	}
	
	public void updateMatchdays(int weekNumber, List<UpdateMatchday> matchs) {
		List<Team> teams = teamDAO.findAll();
		Week week = weekDAO.findByWeekNmr(weekNumber);
		
		List<Matchday> matchdays = new ArrayList<Matchday>();
		
		for(UpdateMatchday um : matchs) {
			Matchday m = new Matchday();
			
			m.setAwayTeam(findTeamFromId(teams, um.getAwayTeamId()));
			m.setHomeTeam(findTeamFromId(teams, um.getHomeTeamId()));
			m.setTime(um.getTime());
			m.setDate(um.getDate());
			m.setWeek(week);
			m.setAwayPoint(0);
			m.setHomePoint(0);
			
			matchdays.add(m);			
		}
		
		matchdayDAO.saveMoreMatchday(weekNumber, matchdays);
	}
	
	protected Team findTeamFromId(List<Team> teams, int teamId) {
		for(Team t : teams) {
			if (t.getId() == teamId) return t;
		}
		return null;
	}
}