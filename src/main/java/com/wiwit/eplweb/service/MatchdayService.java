package com.wiwit.eplweb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wiwit.eplweb.dao.MatchdayDAO;
import com.wiwit.eplweb.model.Matchday;
import com.wiwit.eplweb.model.view.MatchdayModelView;

@Component
public class MatchdayService {

	@Autowired
	private MatchdayDAO machtdayDAO;
	
	@Transactional
	public MatchdayModelView getMatchtdayOnCurrWeek(){
		List<Matchday>  listMatchday = machtdayDAO.getMatchtdayOnCurrWeek();
		return new MatchdayModelView(listMatchday);
	}
}
