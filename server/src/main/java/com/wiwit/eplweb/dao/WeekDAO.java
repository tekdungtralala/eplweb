package com.wiwit.eplweb.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.wiwit.eplweb.model.Week;

@Service
public class WeekDAO extends AbstractDAO{

	private static final Logger logger = LoggerFactory.getLogger(WeekDAO.class);

	@Transactional
	public List<Week> findLastFiveWeek() {
		openSession();
		List<Week> result =  getSession().createQuery("from Week order by startDay")
				.setMaxResults(5).list();
		return result;
	}

	@Transactional
	public Week findByWeekNmr(int weekNumber) {
		openSession();
		Week result = (Week) getSession().createQuery("from Week where weekNumber = " + weekNumber)
				.setMaxResults(1).list().get(0);
		return result;
	}

	@Transactional
	public List<Week> findAllWeek() {
		openSession();
		List<Week> result = getSession().createQuery(
				"from Week order by startDay desc").list();
		logger.info("Week loaded successfully, weeks size=" + result.size());
		return result;
	}

	@Transactional
	public List<Week> findAllPassedWeek(int prevWeek) {
		openSession();

		List<Week> result = getSession().createQuery(
				"from Week as w where w.weekNumber <= " + prevWeek
						+ " order by w.weekNumber desc").list();

		logger.info("Week loaded successfully, weeks size=" + result.size());
		return result;
	}
}
