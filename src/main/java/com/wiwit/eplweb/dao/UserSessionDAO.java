package com.wiwit.eplweb.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wiwit.eplweb.model.User;
import com.wiwit.eplweb.model.UserSession;

@Service
public class UserSessionDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Transactional
	public List<UserSession> findAll() {
		Session session = this.sessionFactory.getCurrentSession();
		return session.createQuery("from UserSession").list();
	}

	@Transactional
	public UserSession findBySession(String session) {
		Session se = this.sessionFactory.getCurrentSession();
		List<UserSession> list = se.createQuery(
				"from UserSession where session='" + session + "'").list();

		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

	@Transactional
	public void saveSession(UserSession us) {
		Session se = sessionFactory.openSession();
		Transaction tx = se.beginTransaction();

		// Delete previous session
		Query q = se.createQuery("DELETE UserSession where user.id = "
				+ us.getUser().getId() + "");
		q.executeUpdate();

		// Save new session
		se.persist(us);

		tx.commit();
		se.close();
	}

	@Transactional
	public void deleteSession(String session) {
		Session se = this.sessionFactory.getCurrentSession();
		Query q = se.createQuery("DELETE UserSession where session = '"
				+ session + "'");
		q.executeUpdate();
	}

}
