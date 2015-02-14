package com.wiwit.eplweb.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.wiwit.eplweb.model.Rank;

@Service
public class RankDAO {

	private static final Logger logger = LoggerFactory.getLogger(RankDAO.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private PhaseDAO phaseDAO;

	@Transactional
	public List<Rank> getFiveHighestLastRank() {
		logger.info("Ready to load Rank - getFiveHighestLastRank()");

		String currentMatchday = phaseDAO.getCurrentMatchday().getValue();

		// last rank must be on previous week
		int prevWeek = Integer.valueOf(currentMatchday) - 1;
		// TODO - check if prevWeek == 0

		return getRankByWeekNumber(prevWeek);
	}

	@Transactional
	public List<Rank> getLatestRank() {
		String currentMatchday = phaseDAO.getCurrentMatchday().getValue();

		// last rank must be on previous week
		int prevWeek = Integer.valueOf(currentMatchday) - 1;
		// TODO - check if prevWeek == 0

		return getRankByWeekNumber(prevWeek);
	}

	@Transactional
	public List<Rank> getRankByWeekNumber(int weekNumber) {
		Session session = this.sessionFactory.getCurrentSession();
		List<Rank> result = session.createQuery(
				"from Rank as r where r.week.weekNumber = " + weekNumber
						+ " ORDER BY r.points DESC").list();
		logger.info("Rank loaded successfully, Ranks size=" + result.size());
		return result;
	}
}
