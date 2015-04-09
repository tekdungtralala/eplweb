package com.wiwit.eplweb.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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
	public List<Matchday> findClosestMatch(int teamId, int weekNumber,
			int totalMatch) {
		Session session = this.sessionFactory.openSession();

		String query = "from Matchday as m where m.week.weekNumber > "
				+ (weekNumber - totalMatch) + " and m.week.weekNumber < "
				+ (weekNumber + totalMatch) + " and (homeTeam.id=" + teamId
				+ " or awayTeam.id=" + teamId + ") order by m.week.weekNumber asc";
		List<Matchday> result = session.createQuery(query).list();
		logger.info("Matchday loaded successfully, matchdays size="
				+ result.size());
		session.close();
		return result;
	}

	@Transactional
	public List<Matchday> findMatchtdayByWeekNmr(int weekNumber) {
		Session session = this.sessionFactory.openSession();

		List<Matchday> result = session.createQuery(findMatchdayByWeekNmr(weekNumber)).list();
		logger.info("Matchday loaded successfully, matchdays size="
				+ result.size());
		session.close();
		return result;
	}
	
	protected String findMatchdayByWeekNmr(int weekNumber){
		return "from Matchday as m where m.week.weekNumber = " + weekNumber
				+ " order by m.date asc, m.time asc";
	}
	
	@Transactional
	public Matchday findMatchtdayById(int matchdayId) {
		Session session = this.sessionFactory.openSession();

		List<Matchday> result = session.createQuery(
				"from Matchday as m where m.id = " + matchdayId).list();
		if (result == null || result.size() == 0){
			logger.info("Can't find matchday with id="+ matchdayId);
			return null;
		}
		session.close();
		return result.get(0);
	}
	
	@Transactional
	public void updateMatchday(Matchday matchday) {
		Session session = this.sessionFactory.openSession();
		session.update(matchday);
		session.close();
	}
	
	@Transactional
	public void saveMoreMatchday(int weekNumber, List<Matchday> matchdays) {
		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		// Delete old data
		List<Matchday> result = session.createQuery(findMatchdayByWeekNmr(weekNumber)).list();
		for(Matchday m : result) {
			session.delete(m);
		}
		
		// Create new data
		for(Matchday m : matchdays) {
			session.persist(m);
		}
		tx.commit();
		session.close();
	}
}
