package com.wiwit.eplweb.model.view;

import java.util.ArrayList;
import java.util.List;

public class ChartData {

	private String name;
	private List<Double> data;

	public ChartData() {
		data = new ArrayList<Double>();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public List<Double> getData() {
		return data;
	}
}
