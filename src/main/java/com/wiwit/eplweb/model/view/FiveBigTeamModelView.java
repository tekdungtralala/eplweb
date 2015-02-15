package com.wiwit.eplweb.model.view;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.wiwit.eplweb.model.Rank;

public class FiveBigTeamModelView {

	private static final Logger logger = LoggerFactory
			.getLogger(FiveBigTeamModelView.class);

	public List<Integer> categories;
	public List<ChartData> series;

	public FiveBigTeamModelView() {
		categories = new ArrayList<Integer>();
		series = new ArrayList<ChartData>();
	}

	public void addData(int week, List<Rank> ranks) {
		
		// initialize series when empty
		if (series.size() == 0) {
			for (Rank r : ranks) {
				ChartData cd = new ChartData(r.getTeam().getName());
				series.add(cd);
			}
		}

		// add week to categories
		categories.add(week);

		// take point from ranks and put to chartdata
		for (Rank r : ranks) {
			for (ChartData cd : series) {
				if (r.getTeam().getName().equals(cd.getName())) {
					cd.getData().add(r.getPoints());
				}
			}
		}
	}

	class ChartData {
		private String name;
		private List<Integer> data;

		public ChartData(String name) {
			this.name = name;
			data = new ArrayList<Integer>();
		}

		public String getName() {
			return name;
		}

		public List<Integer> getData() {
			return data;
		}
	}

	public List<ChartData> getSeries() {
		return series;
	}

	public List<Integer> getCategories() {
		return categories;
	}
}
