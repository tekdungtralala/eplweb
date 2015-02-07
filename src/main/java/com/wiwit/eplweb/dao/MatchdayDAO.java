package com.wiwit.eplweb.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.wiwit.eplweb.model.Matchday;

@Service
public class MatchdayDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private PhaseDAO phaseDAO;

	public List<Matchday> getMatchtdayOnCurrWeek() {
		String currentWeek = phaseDAO.getCurrentMatchday().getValue();
		Session session = this.sessionFactory.getCurrentSession();
		return session.createQuery(
				"from Matchday as m where m.week.weekNumber = " + currentWeek
						+ " order by m.date asc, m.time asc").list();
	}
}
