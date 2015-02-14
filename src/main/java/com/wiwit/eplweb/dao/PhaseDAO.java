package com.wiwit.eplweb.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wiwit.eplweb.model.Phase;

@Service
public class PhaseDAO {

	private enum PhaseKey {
		CURRENT_MATCHDAY, CURRENT_SEASON
	}

	@Autowired
	private SessionFactory sessionFactory;

	private Phase getPhaseByKey(PhaseKey phaseKey) {
		Session session = this.sessionFactory.getCurrentSession();
		List<Phase> results = session
				.createQuery("from Phase where key like '" + phaseKey.toString()+"'")
				.setMaxResults(1).list();
		return results.get(0);
	}

	public Phase getCurrentMatchday() {
		return this.getPhaseByKey(PhaseKey.CURRENT_MATCHDAY);
	}

	public Phase getCurrentSeason() {
		return this.getPhaseByKey(PhaseKey.CURRENT_SEASON);
	}
}
