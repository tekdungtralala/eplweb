package com.wiwit.eplweb.model.view;

import java.util.ArrayList;
import java.util.List;

import com.wiwit.eplweb.model.Rank;

public class FiveBigTeamModelView {

	private List<Integer> categories;
	private List<ChartData> series;

	public FiveBigTeamModelView() {
		categories = new ArrayList<Integer>();
		series = new ArrayList<ChartData>();
	}

	public void addData(int week, List<Rank> ranks) {
		
		// initialize series when empty
		if (series.size() == 0) {
			for (Rank r : ranks) {
				ChartData cd = new ChartData();
				cd.setName(r.getTeam().getName());
				series.add(cd);
			}
		}

		// add week to categories
		categories.add(week);

		// take point from ranks and put to chartdata
		for (Rank r : ranks) {
			for (ChartData cd : series) {
				if (r.getTeam().getName().equals(cd.getName())) {
					cd.getData().add(Double.valueOf(r.getPoints()));
				}
			}
		}
	}

	public List<ChartData> getSeries() {
		return series;
	}

	public List<Integer> getCategories() {
		return categories;
	}
}
