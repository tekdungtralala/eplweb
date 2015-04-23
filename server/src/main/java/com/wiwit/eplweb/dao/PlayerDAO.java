package com.wiwit.eplweb.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.wiwit.eplweb.model.Player;

@Service
public class PlayerDAO extends AbstractDAO{

	@Transactional
	public List<Player> findSquadsByTeamId(int teamId) {
		openSession();
		List<Player> result = getSession().createQuery(
				"from Player where team.id=" + teamId
						+ " order by playerNumber asc").list();
		commitAndClose();
		return result;
	}

	@Transactional
	public Player findById(int id) {
		openSession();
		List<Player> result = getSession().createQuery("from Player where id=" + id)
				.list();
		commitAndClose();
		return result.get(0);
	}

	@Transactional
	public void updatePlayer(Player player) {
		openSession();
		getSession().update(player);
		commitAndClose();
	}

	@Transactional
	public void savePlayer(Player player) {
		openSession();
		getSession().persist(player);
		commitAndClose();
	}

	@Transactional
	public Player findByTeamAndNumber(int teamId, int playerNumber) {
		openSession();
		List<Player> result = getSession().createQuery(
				"from Player where team.id=" + teamId + " and playerNumber="
						+ playerNumber).list();

		commitAndClose();
		if (result == null || result.size() == 0)
			return null;

		return result.get(0);
	}

	@Transactional
	public void deletePlayer(Player player) {
		openSession();
		getSession().delete(player);
		commitAndClose();
	}
}
