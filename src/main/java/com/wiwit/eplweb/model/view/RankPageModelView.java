package com.wiwit.eplweb.model.view;

import java.util.List;

import com.wiwit.eplweb.model.Rank;
import com.wiwit.eplweb.model.Week;

public class RankPageModelView {

	private List<Rank> ranks;
	private List<Week> weeks;
	
	public void setRanks(List<Rank> ranks) {
		this.ranks = ranks;
	}
	
	public List<Rank> getRanks() {
		return ranks;
	}
	
	public void setWeeks(List<Week> weeks) {
		this.weeks = weeks;
	}
	
	public List<Week> getWeeks() {
		return weeks;
	}
}
