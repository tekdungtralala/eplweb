package com.wiwit.eplweb.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.wiwit.eplweb.model.Season;
import com.wiwit.eplweb.model.Week;

@Repository("weekDAO")
@Service
@Transactional
public class WeekDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	public List<Week> getLastFiveWeek(){
		Session session = this.sessionFactory.getCurrentSession(); 
		return session.createQuery("from Week order by startDay").setMaxResults(5).list();
	}
}
