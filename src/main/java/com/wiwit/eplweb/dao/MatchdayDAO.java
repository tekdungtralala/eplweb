package com.wiwit.eplweb.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wiwit.eplweb.model.Matchday;

@Service
public class MatchdayDAO {
	
	private static final Logger logger = LoggerFactory.getLogger(MatchdayDAO.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private PhaseDAO phaseDAO;

	public List<Matchday> getMatchtdayOnCurrWeek() {
		String currentWeek = phaseDAO.getCurrentMatchday().getValue();
		Session session = this.sessionFactory.getCurrentSession();
		
		List<Matchday> result = session.createQuery(
				"from Matchday as m where m.week.weekNumber = " + currentWeek
				+ " order by m.date asc, m.time asc").list();
		logger.info("Matchday loaded successfully, matchdays size=" + result.size());
		return result;
	}
}
