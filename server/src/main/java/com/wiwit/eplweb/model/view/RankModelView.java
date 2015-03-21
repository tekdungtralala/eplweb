package com.wiwit.eplweb.model.view;

import java.util.List;

import com.wiwit.eplweb.model.Rank;

public class RankModelView {

	public List<Rank> ranks;
	
	public static RankModelView getModelView(List<Rank> ranks){
		RankModelView result = new RankModelView();
		result.ranks = ranks;
		return result;
	}
}
