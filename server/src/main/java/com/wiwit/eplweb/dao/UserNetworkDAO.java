package com.wiwit.eplweb.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wiwit.eplweb.model.User;
import com.wiwit.eplweb.model.UserNetwork;
import com.wiwit.eplweb.model.input.UserNetworkModelInput;
import com.wiwit.eplweb.util.UserNetworkType;

@Service
public class UserNetworkDAO {

	private static final Logger logger = LoggerFactory.getLogger(UserNetworkDAO.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Transactional
	public void create(UserNetwork userNetwork, boolean newUser, 
			UserNetworkModelInput model) {
		
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		if (newUser) {
			User user = new User();
			user.setFirstName(model.getFirstName());
			user.setLastName(model.getLastName());
			user.setImageUrl(model.getImageUrl());
			session.persist(user);
			
			userNetwork.setUser(user);
		}
		session.persist(userNetwork);
		
		tx.commit();
		session.close();
	}
	
	@Transactional
	public void create(UserNetwork un, User usr) {
		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		session.persist(un);
		session.persist(usr);
		
		tx.commit();
		session.close();
	}
	
	@Transactional
	public UserNetwork findByEmailAndType(String email, UserNetworkType type) {
		Session session = this.sessionFactory.openSession();
		List<UserNetwork> results = session.createQuery("from UserNetwork where " +
				"email='" + email + "' and type='" + type.getValue() + "'").list();
		session.close();
		if (results != null && results.size() > 0)
			return results.get(0);
		return null;
	}
	
	@Transactional
	public UserNetwork findByEmail(String email) {
		Session session = this.sessionFactory.openSession();
		List<UserNetwork> results = session.createQuery("from UserNetwork where " +
				"email='" + email + "'").list();
		session.close();
		if (results != null && results.size() > 0)
			return results.get(0);
		return null;
	}
}
