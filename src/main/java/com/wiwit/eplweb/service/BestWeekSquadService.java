package com.wiwit.eplweb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wiwit.eplweb.dao.BestWeekSquadDAO;
import com.wiwit.eplweb.dao.PhaseDAO;
import com.wiwit.eplweb.model.BestWeekSquad;

@Component
public class BestWeekSquadService {

	@Autowired
	private BestWeekSquadDAO bestWeekSquadDAO;

	@Autowired
	private PhaseDAO phaseDAO;

	@Transactional
	public List<BestWeekSquad> getBestSquadByWeekId(int weekId) {
		return bestWeekSquadDAO.getBestSquadByWeekId(weekId);
	}

	@Transactional
	public List<BestWeekSquad> getBestSquadLastWeek() {
		int lastWeek = Integer
				.valueOf(phaseDAO.getCurrentMatchday().getValue()) - 1;
		// TODO what must we do if last week = 0
		return this.getBestSquadByWeekId(lastWeek);
	}
}
