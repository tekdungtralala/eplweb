package com.wiwit.eplweb.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AbstractDAO {
	@Autowired
	private SessionFactory sessionFactory;
	
	private Transaction tx;
	private Session session;

	public void openSession() {
		session = this.sessionFactory.openSession();
		tx = session.beginTransaction();
	}
	
	public void commitAndClose() {
		tx.commit();
		session.close();
	}
	
	public Session getSession() {
		return session;
	}

}
