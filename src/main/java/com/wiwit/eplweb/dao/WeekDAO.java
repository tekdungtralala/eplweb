package com.wiwit.eplweb.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wiwit.eplweb.model.Week;

@Service
public class WeekDAO {

	private static final Logger logger = LoggerFactory.getLogger(WeekDAO.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Transactional
	public List<Week> getLastFiveWeek() {
		Session session = this.sessionFactory.getCurrentSession();
		return session.createQuery("from Week order by startDay")
				.setMaxResults(5).list();
	}

	@Transactional
	public Week findByWeekNmr(int weekNumber) {
		Session session = this.sessionFactory.getCurrentSession();
		Week result = (Week) session.createQuery("from Week where weekNumber = " + weekNumber)
				.setMaxResults(1).list().get(0);
		return result;
	}

	@Transactional
	public List<Week> getAllWeek() {
		Session session = this.sessionFactory.getCurrentSession();
		List<Week> result = session.createQuery(
				"from Week order by startDay desc").list();
		logger.info("Week loaded successfully, weeks size=" + result.size());
		return result;
	}

	@Transactional
	public List<Week> getAllPassedWeek(int prevWeek) {
		Session session = this.sessionFactory.getCurrentSession();

		List<Week> result = session.createQuery(
				"from Week as w where w.weekNumber <= " + prevWeek
						+ " order by w.weekNumber desc").list();

		logger.info("Week loaded successfully, weeks size=" + result.size());
		return result;
	}
}
