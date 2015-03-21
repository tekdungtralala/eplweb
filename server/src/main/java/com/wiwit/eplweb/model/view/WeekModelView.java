package com.wiwit.eplweb.model.view;

import java.util.List;

import com.wiwit.eplweb.model.Week;

public class WeekModelView {

	public List<Week> weeks;
	
	public static WeekModelView getModelView(List<Week> weeks){
		WeekModelView result = new WeekModelView();
		result.weeks = weeks;
		return result;
	}
}
