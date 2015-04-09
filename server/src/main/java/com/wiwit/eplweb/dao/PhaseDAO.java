package com.wiwit.eplweb.dao;

import java.util.List;

import javax.transaction.Transactional;

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

	@Transactional
	private Phase findPhaseByKey(PhaseKey phaseKey) {
		Session session = this.sessionFactory.openSession();
		List<Phase> results = session
				.createQuery("from Phase where key like '" + phaseKey.toString()+"'")
				.setMaxResults(1).list();
		session.close();
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
