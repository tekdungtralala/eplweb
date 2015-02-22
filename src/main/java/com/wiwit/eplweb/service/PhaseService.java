package com.wiwit.eplweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wiwit.eplweb.dao.PhaseDAO;
import com.wiwit.eplweb.model.Phase;

@Component
public class PhaseService {

	@Autowired
	private PhaseDAO phaseDAO;

	public Phase getCurrentMatchday() {
		return phaseDAO.getCurrentMatchday();
	}

	public Phase getCurrentSeason() {
		return phaseDAO.getCurrentSeason();
	}
}
