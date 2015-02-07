package com.wiwit.eplweb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wiwit.eplweb.dao.BestWeekSquadDAO;
import com.wiwit.eplweb.model.BestWeekSquad;

@Component
public class BestWeekSquadService {

	@Autowired
	private BestWeekSquadDAO bestWeekSquadDAO;
	
	@Transactional
	public List<BestWeekSquad> getBestSquadByWeekId(int weekId){
		return bestWeekSquadDAO.getBestSquadByWeekId(weekId);
	}
}
