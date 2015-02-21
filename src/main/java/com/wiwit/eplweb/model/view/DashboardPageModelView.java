package com.wiwit.eplweb.model.view;

import java.util.List;

import com.wiwit.eplweb.model.Rank;
import com.wiwit.eplweb.model.Week;

public class DashboardPageModelView {

	private FiveBigTeamModelView fiveBigTeam;
	private List<Rank> highestRank;
	private MatchdayModelView matchday;
	private Week currentWeek;
	
	public DashboardPageModelView() {
	}
	
	public void setFiveBigTeam(FiveBigTeamModelView fiveBigTeam) {
		this.fiveBigTeam = fiveBigTeam;
	}
	public FiveBigTeamModelView getFiveBigTeam() {
		return fiveBigTeam;
	}
	
	public void setHighestRank(List<Rank> highestRank) {
		this.highestRank = highestRank;
	}
	public List<Rank> getHighestRank() {
		return highestRank;
	}
	
	public void setMatchday(MatchdayModelView matchday) {
		this.matchday = matchday;
	}
	public MatchdayModelView getMatchday() {
		return matchday;
	}
	
	public void setCurrentWeek(Week currentWeek) {
		this.currentWeek = currentWeek;
	}
	public Week getCurrentWeek() {
		return currentWeek;
	}

}
