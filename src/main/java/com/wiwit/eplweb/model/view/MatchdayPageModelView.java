package com.wiwit.eplweb.model.view;

import java.util.List;

import com.wiwit.eplweb.model.Week;

public class MatchdayPageModelView {

	private MatchdayModelView matchdayModelView;
	private List<Week> weeks;
	
	public void setMatchdayModelView(MatchdayModelView matchdayModelView) {
		this.matchdayModelView = matchdayModelView;
	}
	public MatchdayModelView getMatchdayModelView() {
		return matchdayModelView;
	}
	
	public void setWeeks(List<Week> weeks) {
		this.weeks = weeks;
	}
	public List<Week> getWeeks() {
		return weeks;
	}
}
