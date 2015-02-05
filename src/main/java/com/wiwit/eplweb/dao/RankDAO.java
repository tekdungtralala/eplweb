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

@Repository("rankDAO")
@Service
@Transactional
public class RankDAO {

	private static final Logger logger = LoggerFactory.getLogger(RankDAO.class);

	@Autowired
	private SessionFactory sessionFactory;

	public List<Rank> getFiveHighestRank() {
		logger.info("Ready to load Rank - getFiveHighestRank()");
		
		Session session = this.sessionFactory.getCurrentSession();
		List<Rank> result = session.createQuery("from Rank ORDER BY week DESC, points DESC").setMaxResults(5).list();
		
		logger.info("Rank loaded successfully, Ranks size=" + result.size());
		return result;
	}
}
