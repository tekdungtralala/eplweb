package com.wiwit.eplweb.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wiwit.eplweb.model.Matchday;

@Service
public class MatchdayDAO {

	private static final Logger logger = LoggerFactory
			.getLogger(MatchdayDAO.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Transactional
	public List<Matchday> getLastAndNextMatchday(int teamId, int weekNumber,
			int totalMatch) {
		Session session = this.sessionFactory.getCurrentSession();

		String query = "from Matchday as m where m.week.weekNumber > "
				+ (weekNumber - totalMatch) + " and m.week.weekNumber < "
				+ (weekNumber + totalMatch) + " and (homeTeam.id=" + teamId
				+ " or awayTeam.id=" + teamId + ") order by m.week.weekNumber asc";
		List<Matchday> result = session.createQuery(query).list();
		logger.info("Matchday loaded successfully, matchdays size="
				+ result.size());
		return result;
	}

	@Transactional
	public List<Matchday> getMatchtdayByWeekNmr(int weekNumber) {
		Session session = this.sessionFactory.getCurrentSession();

		List<Matchday> result = session.createQuery(
				"from Matchday as m where m.week.weekNumber = " + weekNumber
						+ " order by m.date asc, m.time asc").list();
		logger.info("Matchday loaded successfully, matchdays size="
				+ result.size());
		return result;
	}
	
	@Transactional
	public Matchday findMatchtdayById(int matchdayId) {
		Session session = this.sessionFactory.getCurrentSession();

		List<Matchday> result = session.createQuery(
				"from Matchday as m where m.id = " + matchdayId).list();
		if (result == null || result.size() == 0){
			logger.info("Can't find matchday with id="+ matchdayId);
		}
		return result.get(0);
	}
	
	@Transactional
	public void updateMatchday(Matchday matchday) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(matchday);
	}
}
