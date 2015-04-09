package com.wiwit.eplweb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wiwit.eplweb.dao.BestWeekSquadDAO;
import com.wiwit.eplweb.dao.PhaseDAO;
import com.wiwit.eplweb.model.BestWeekSquad;

@Component
public class BestWeekSquadService {

	@Autowired
	private BestWeekSquadDAO bestWeekSquadDAO;

	@Autowired
	private PhaseDAO phaseDAO;

	public List<BestWeekSquad> findBestSquadByWeekId(int weekId) {
		return bestWeekSquadDAO.findBestSquadByWeekId(weekId);
	}

	public List<BestWeekSquad> findBestSquadLastWeek() {
		int lastWeek = Integer
				.valueOf(phaseDAO.findCurrentMatchday().getValue()) - 1;
		// TODO what must we do if last week = 0
		return this.findBestSquadByWeekId(lastWeek);
	}
}
