package com.wiwit.eplweb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wiwit.eplweb.dao.WeekDAO;
import com.wiwit.eplweb.model.Week;

@Component
public class WeekService {

	@Autowired
	private WeekDAO weekDAO;
	
	@Transactional
	public List<Week> getLastFiveWeek(){
		return weekDAO.getLastFiveWeek();
	}
	
	@Transactional
	public List<Week> getAllPassedWeek(){
		return weekDAO.getAllPassedWeek();
	}
}
