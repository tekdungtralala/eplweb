package com.wiwit.eplweb.model.view;

import java.util.List;

import com.wiwit.eplweb.model.Matchday;
import com.wiwit.eplweb.model.Rank;
import com.wiwit.eplweb.model.Team;

public class TeamPageModelView {

	private List<Team> teams;
	private Rank rank;
	private List<Matchday> matchdays;
	
	public void setTeams(List<Team> teams) {
		this.teams = teams;
	}
	public List<Team> getTeams() {
		return teams;
	}
	
	public void setRank(Rank rank) {
		this.rank = rank;
	}
	public Rank getRank() {
		return rank;
	}
	
	public void setMatchdays(List<Matchday> matchdays) {
		this.matchdays = matchdays;
	}
	public List<Matchday> getMatchdays() {
		return matchdays;
	}
}
