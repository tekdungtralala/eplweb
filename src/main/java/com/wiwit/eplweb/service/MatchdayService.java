package com.wiwit.eplweb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wiwit.eplweb.dao.MatchdayDAO;
import com.wiwit.eplweb.dao.PhaseDAO;
import com.wiwit.eplweb.dao.WeekDAO;
import com.wiwit.eplweb.model.Matchday;
import com.wiwit.eplweb.model.Week;
import com.wiwit.eplweb.model.view.MatchdayModelView;

@Component
public class MatchdayService {

	@Autowired
	private MatchdayDAO machtdayDAO;
	
	@Autowired
	private PhaseDAO phaseDAO;
	
	@Autowired
	private WeekDAO weekDAO;
	
	@Transactional
	public MatchdayModelView getMatchtdayOnCurrWeek(){
		String currentWeek = phaseDAO.getCurrentMatchday().getValue();
		return getMatchtdayByWeekNmr(Integer.valueOf(currentWeek));
	}
	
	@Transactional
	public MatchdayModelView getMatchtdayByWeekNmr(int weekNumber){
		Week week = weekDAO.findByWeekNmr(weekNumber);
		
		List<Matchday>  listMatchday = machtdayDAO.getMatchtdayByWeekNmr(Integer.valueOf(weekNumber));
		return new MatchdayModelView(listMatchday, week);
	}
}
