package com.wiwit.eplweb.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wiwit.eplweb.model.Player;

@Service
public class PlayerDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Transactional
	public List<Player> findSquadsByTeamId(int teamId) {
		Session session = this.sessionFactory.openSession();
		List<Player> result = session.createQuery(
				"from Player where team.id=" + teamId
						+ " order by playerNumber asc").list();
		session.close();
		return result;
	}

	@Transactional
	public Player findById(int id) {
		Session session = this.sessionFactory.openSession();
		List<Player> result = session.createQuery("from Player where id=" + id)
				.list();
		session.close();
		return result.get(0);
	}

	@Transactional
	public void updatePlayer(Player player) {
		Session session = this.sessionFactory.openSession();
		session.update(player);
		session.close();
	}

	@Transactional
	public void savePlayer(Player player) {
		Session session = this.sessionFactory.openSession();
		session.persist(player);
		session.close();
	}

	@Transactional
	public Player findByTeamAndNumber(int teamId, int playerNumber) {
		Session session = this.sessionFactory.openSession();
		List<Player> result = session.createQuery(
				"from Player where team.id=" + teamId + " and playerNumber="
						+ playerNumber).list();

		session.close();
		if (result == null || result.size() == 0)
			return null;

		return result.get(0);
	}

	@Transactional
	public void deletePlayer(Player player) {
		Session session = this.sessionFactory.openSession();
		session.delete(player);
		session.close();
	}
}
