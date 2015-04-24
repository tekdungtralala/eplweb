package com.wiwit.eplweb.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.wiwit.eplweb.model.Phase;

@Service
public class PhaseDAO extends AbstractDAO{

	private enum PhaseKey {
		CURRENT_MATCHDAY, CURRENT_SEASON
	}

	@Transactional
	private Phase findPhaseByKey(PhaseKey phaseKey) {
		openSession();
		List<Phase> results = getSession()
				.createQuery("from Phase where key like '" + phaseKey.toString()+"'")
				.setMaxResults(1).list();
		return results.get(0);
	}

	@Transactional
	public Phase findCurrentMatchday() {
		return this.findPhaseByKey(PhaseKey.CURRENT_MATCHDAY);
	}

	@Transactional
	public Phase findCurrentSeason() {
		return this.findPhaseByKey(PhaseKey.CURRENT_SEASON);
	}
}
