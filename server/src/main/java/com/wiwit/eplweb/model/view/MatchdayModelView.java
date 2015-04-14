package com.wiwit.eplweb.model.view;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.wiwit.eplweb.model.Matchday;
import com.wiwit.eplweb.model.MatchdayVoting;
import com.wiwit.eplweb.model.Week;

public class MatchdayModelView {

	private HashMap<String, List<Matchday>> model;
	private Week week;
	private List<MatchdayVoting> votings;

	public MatchdayModelView(List<Matchday> list, Week week) {
		this.model = new HashMap<String, List<Matchday>>();
		generate(list);
		
		this.week = week;
	}
	
	public void setWeek(Week week) {
		this.week = week;
	}
	
	public Week getWeek() {
		return week;
	}

	public HashMap<String, List<Matchday>> getModel() {
		return model;
	}

	public void setModel(HashMap<String, List<Matchday>> model) {
		this.model = model;
	}
	
	public List<MatchdayVoting> getVotings() {
		return votings;
	}
	
	public void setVotings(List<MatchdayVoting> votings) {
		this.votings = votings;
	}

	private void generate(List<Matchday> list) {
		for (Matchday m : list) {
			DateFormat df = new SimpleDateFormat("E MMM dd,yyyy");
			String key = df.format(m.getDate());

			List<Matchday> value = model.get(key);

			if (value == null) {
				value = new ArrayList<Matchday>();
				model.put(key, value);
			}
			
			value.add(m);
		}
	}
}
