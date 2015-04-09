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
import com.wiwit.eplweb.util.UserRoleHelper;

@Service
public class UserSessionDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Transactional
	public List<UserSession> findAll() {
		Session session = this.sessionFactory.openSession();
		List<UserSession> result =  session.createQuery("from UserSession").list();
		session.close();
		return result;
	}
	
	@Transactional
	public UserSession findById(int id) {
		Session se = this.sessionFactory.openSession();
		List<UserSession> list = se.createQuery(
				"from UserSession where id=" + id + "").list();

		se.close();
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

	@Transactional
	public UserSession findBySession(String session) {
		Session se = this.sessionFactory.openSession();
		List<UserSession> list = se.createQuery(
				"from UserSession where session='" + session + "'").list();
		se.close();
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

	@Transactional
	public void saveSession(UserSession us) {
		Session se = this.sessionFactory.openSession();
		Transaction tx = se.beginTransaction();

		// Delete previous session
		if (us.getUser() != null){
			Query q = se.createQuery("DELETE UserSession where user.id = "
					+ us.getUser().getId() + "");
			q.executeUpdate();
			us.setRole(UserRoleHelper.getAdminRole());
		} else if (us.getUserNetwork() != null) {
			Query q = se.createQuery("DELETE UserSession where userNetwork.id = "
					+ us.getUserNetwork().getId() + "");
			q.executeUpdate();
			us.setRole(UserRoleHelper.getUserRole());
		}

		// Save new session
		se.persist(us);

		tx.commit();
		se.close();
	}

	@Transactional
	public void deleteSession(String session) {
		Session se = this.sessionFactory.openSession();
		Query q = se.createQuery("DELETE UserSession where session = '"
				+ session + "'");
		q.executeUpdate();
		se.close();
	}

}
