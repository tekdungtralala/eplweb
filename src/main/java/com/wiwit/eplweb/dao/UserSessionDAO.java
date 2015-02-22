package com.wiwit.eplweb.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wiwit.eplweb.model.UserSession;

@Service
public class UserSessionDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Transactional
	public List<UserSession> findAll(){
		Session session = this.sessionFactory.getCurrentSession();
		return session.createQuery("from UserSession").list();
	}
}
