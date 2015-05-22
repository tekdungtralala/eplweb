package com.wiwit.eplweb.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Any DAO class on this package must be extend this class
@Service
public class AbstractDAO {
	@Autowired
	private SessionFactory sessionFactory;
	
	private Transaction tx;
	private Session session;

	// For READ request we use openSession().
	// No need to close the session because it will be used by another
	//  READ request.
	public void openSession() {
		session = this.sessionFactory.getCurrentSession();
	}
	
	// For CREATE, UPDATE and DELETE must use new session.
	// And need to be close the session.
	public void openSession(boolean withTx) {
		session = this.sessionFactory.openSession();
		if (withTx)
			tx = session.beginTransaction();
	}
	
	// Commit transaction
	public void commit() {
		tx.commit();
	}
	
	// Roleback transaction
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
