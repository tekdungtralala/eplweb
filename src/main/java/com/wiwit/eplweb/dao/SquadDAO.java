package com.wiwit.eplweb.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wiwit.eplweb.model.Squad;

@Service
public class SquadDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public List<Squad> getSquadsByTeamId(int teamId) {
		Session session = this.sessionFactory.getCurrentSession();
		return session.createQuery("from Squad ").list();
	}
}
