package com.wiwit.eplweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wiwit.eplweb.dao.PhaseDAO;
import com.wiwit.eplweb.model.Phase;

@Component
public class PhaseService {

	@Autowired
	private PhaseDAO phaseDAO;

	public Phase findCurrentMatchday() {
		return phaseDAO.findCurrentMatchday();
	}

	public Phase findCurrentSeason() {
		return phaseDAO.findCurrentSeason();
	}
}
