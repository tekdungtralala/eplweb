package com.wiwit.eplweb.model.view;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.wiwit.eplweb.model.Matchday;

public class MatchdayModelView {

	private HashMap<String, List<Matchday>> model;

	public MatchdayModelView(List<Matchday> list) {
		this.model = new HashMap<String, List<Matchday>>();
		generate(list);
	}

	public HashMap<String, List<Matchday>> getModel() {
		return model;
	}

	public void setModel(HashMap<String, List<Matchday>> model) {
		this.model = model;
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
