package com.wiwit.eplweb.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.wiwit.eplweb.model.Matchday;

@Repository("matchdayDAO")
@Service
@Transactional
public class MatchdayDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	public List<Matchday> getMatchDayOnLastWeek(){
		Session session = this.sessionFactory.getCurrentSession(); 
		return session.createQuery("from Matchday order by date desc").setMaxResults(20).list();
	}
}
