package com.wiwit.eplweb.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wiwit.eplweb.model.Rank;

@Service
public class RankDAO {

	private static final Logger logger = LoggerFactory.getLogger(RankDAO.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Transactional
	public List<Rank> getRankByWeekNumber(int weekNumber) {
		Session session = this.sessionFactory.getCurrentSession();
		List<Rank> result = session.createQuery(
				"from Rank as r where r.week.weekNumber = " + weekNumber
						+ " ORDER BY r.points DESC").list();
		logger.info("Rank loaded successfully, ranks size=" + result.size());
		return result;
	}
}
