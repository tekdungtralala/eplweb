package com.wiwit.eplweb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wiwit.eplweb.dao.PhaseDAO;
import com.wiwit.eplweb.dao.WeekDAO;
import com.wiwit.eplweb.model.Week;

@Component
public class WeekService {

	@Autowired
	private WeekDAO weekDAO;
	
	@Autowired
	private PhaseDAO phaseDAO;
	
	public List<Week> findLastFiveWeek(){
		return weekDAO.findLastFiveWeek();
	}
	
	public Week findCurrWeek(){
		String currentMatchday = phaseDAO.findCurrentMatchday().getValue();
		
		return weekDAO.findByWeekNmr(Integer.valueOf(currentMatchday) - 1);
	}
	
	public Week findByWeekNumber(int weekNumber){
		return weekDAO.findByWeekNmr(weekNumber);
	}
	
	public List<Week> findAllPassedWeek(){
		String currentMatchday = phaseDAO.findCurrentMatchday().getValue();

		// last rank must be on previous week
		int prevWeek = Integer.valueOf(currentMatchday) - 1;
		// TODO - check if prevWeek == 0
		
		return weekDAO.findAllPassedWeek(prevWeek);
	}
	
	public List<Week> getAllWeek(){
		return weekDAO.findAllWeek();
	}
}
