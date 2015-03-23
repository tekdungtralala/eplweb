package com.wiwit.eplweb.model.view;

import java.util.List;

import com.wiwit.eplweb.model.Matchday;
import com.wiwit.eplweb.model.Rank;
import com.wiwit.eplweb.model.Team;
import com.wiwit.eplweb.model.Week;

public class TeamPageModelView {

	private List<Team> teams;
	private List<Rank> ranks;
	private List<Matchday> matchdays;
	private Week currentWeek;
	
	public void setTeams(List<Team> teams) {
		this.teams = teams;
	}
	public List<Team> getTeams() {
		return teams;
	}
	
	public void setRanks(List<Rank> ranks) {
		this.ranks = ranks;
	}
	public List<Rank> getRanks() {
		return ranks;
	}
	
	public void setMatchdays(List<Matchday> matchdays) {
		this.matchdays = matchdays;
	}
	public List<Matchday> getMatchdays() {
		return matchdays;
	}
	
	public Week getCurrentWeek() {
		return currentWeek;
	}
	public void setCurrentWeek(Week currentWeek) {
		this.currentWeek = currentWeek;
	}
}
