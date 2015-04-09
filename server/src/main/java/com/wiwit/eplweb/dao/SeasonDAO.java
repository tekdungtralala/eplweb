package com.wiwit.eplweb.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wiwit.eplweb.model.Season;

@Service
public class SeasonDAO {

	private static final Logger logger = LoggerFactory
			.getLogger(SeasonDAO.class);

	@Autowired
	private SessionFactory sessionFactory;

	public List<Season> findAllSeason() {
		Session session = this.sessionFactory.openSession();
		List<Season> result = session.createQuery("from Season").list();
		session.close();
		return result;
	}

	public Season findSeasonById(Object id) {
		Session session = this.sessionFactory.openSession();
		Season result = (Season) session.createQuery("from Season where id = " + id)
				.list().get(0);
		
		session.close();		
		return result;
	}
}
