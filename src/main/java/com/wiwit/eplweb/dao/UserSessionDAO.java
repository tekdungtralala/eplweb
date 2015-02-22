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
	public void saveSession(UserSession us) {
		Session se = sessionFactory.openSession();
		Transaction tx = se.beginTransaction();

		// Delete all session before
		Query q = se.createQuery("DELETE UserSession where user.id = "
				+ us.getUser().getId() + "");
		q.executeUpdate();

		// Save session to db
		se.persist(us);

		tx.commit();
		se.close();
	}

	@Transactional
	public void DeleteAllSession(User u) {
		Session session = this.sessionFactory.getCurrentSession();
		// session.
	}

}
