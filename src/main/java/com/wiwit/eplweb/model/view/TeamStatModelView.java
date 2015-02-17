package com.wiwit.eplweb.model.view;

import java.util.ArrayList;
import java.util.List;

import com.wiwit.eplweb.model.Rank;

public class TeamStatModelView {

	public static String PTS = "Points";
	public static String WR = "Win Rate";
	public static String WL = "Win Lose";
	public static String WD = "Win Draw";
	public static String GS = "Goal Scored";
	public static String GA = "Goal Against";
	public static String OT = "Other Team";

	private List<String> categories;
	private List<ChartData> series;

	public TeamStatModelView() {
		categories = new ArrayList<String>();
		series = new ArrayList<ChartData>();
	}

	public void addData(int teamId, int weekNumber, List<Rank> ranks) {
		ChartData c1 = new ChartData();
		ChartData c2 = new ChartData();

		int tmpPTS = 0, tmpWR = 0, tmpWL = 0, tmpWD = 0, tmpGS = 0, tmpGA = 0;
		for (Rank r : ranks) {
			if (r.getTeam().getId() == teamId) {
				c1.getData().add((double) r.getPoints());
				c1.getData().add((double) r.getGamesWon());
				c1.getData().add((double) r.getGamesLost());
				c1.getData().add((double) r.getGamesDrawn());
				c1.getData().add((double) r.getGoalsScored());
				c1.getData().add((double) r.getGoalsAgainst());
				
				c1.setName(r.getTeam().getName());
			}

			tmpPTS = tmpPTS + r.getPoints();
			tmpWR = tmpWR + r.getGamesWon();
			tmpWL = tmpWL + r.getGamesLost();
			tmpWD = tmpWD + r.getGamesDrawn();
			tmpGS = tmpGS + r.getGoalsScored();
			tmpGA = tmpGA + r.getGoalsAgainst();
		}

		c2.getData().add((double) (tmpPTS / ranks.size()));
		c2.getData().add((double) (tmpWR / ranks.size()));
		c2.getData().add((double) (tmpWL / ranks.size()));
		c2.getData().add((double) (tmpWD / ranks.size()));
		c2.getData().add((double) (tmpGS / ranks.size()));
		c2.getData().add((double) (tmpGA / ranks.size()));
		c2.setName(OT);
		
		series.add(c1);
		series.add(c2);
		
		categories.add(PTS);
		categories.add(WR);
		categories.add(WL);
		categories.add(WD);
		categories.add(GS);
		categories.add(GA);
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}
	
	public List<String> getCategories() {
		return categories;
	}

	public List<ChartData> getSeries() {
		return series;
	}

	public void setSeries(List<ChartData> series) {
		this.series = series;
	}

}
