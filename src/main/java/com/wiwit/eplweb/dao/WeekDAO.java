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

	@Autowired
	private PhaseDAO phaseDAO;

	public List<Week> getLastFiveWeek() {
		Session session = this.sessionFactory.getCurrentSession();
		return session.createQuery("from Week order by startDay")
				.setMaxResults(5).list();
	}

	public List<Week> getAllPassedWeek() {
		Session session = this.sessionFactory.getCurrentSession();

		String currentMatchday = phaseDAO.getCurrentMatchday().getValue();

		// last rank must be on previous week
		int prevWeek = Integer.valueOf(currentMatchday) - 1;
		// TODO - check if prevWeek == 0

		return session.createQuery(
				"from Week as w where w.weekNumber <= " + prevWeek
						+ " order by w.weekNumber desc").list();
	}
}
