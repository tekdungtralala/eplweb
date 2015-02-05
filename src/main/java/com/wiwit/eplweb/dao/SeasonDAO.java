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

import com.wiwit.eplweb.model.Season;

@Repository("seasonDAO")
@Service
@Transactional
public class SeasonDAO {
	
	private static final Logger logger = LoggerFactory.getLogger(SeasonDAO.class);

	@Autowired
	private SessionFactory sessionFactory;
	
	public List<Season> getAllSeason(){
		Session session = this.sessionFactory.getCurrentSession(); 
		return session.createQuery("from Season").list();
	}
}
