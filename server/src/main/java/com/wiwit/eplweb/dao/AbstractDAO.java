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
		session = this.sessionFactory.getCurrentSession();
	}
	
	public void openSession(boolean withTx) {
		session = this.sessionFactory.openSession();
		if (withTx)
			tx = session.beginTransaction();
	}
	
	public void commit() {
		tx.commit();
	}
	
	public void roleback() {
		tx.rollback();
	}
	
	public void closeConnection() {
		session.close();
	}
	
	public Session getSession() {
		return session;
	}
}
