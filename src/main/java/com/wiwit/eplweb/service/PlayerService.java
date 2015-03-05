package com.wiwit.eplweb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wiwit.eplweb.dao.PlayerDAO;
import com.wiwit.eplweb.model.Player;

@Component
public class PlayerService {

	@Autowired
	private PlayerDAO playerDAO;

	public List<Player> getSquadsByTeamId(int teamId) {
		return playerDAO.getSquadsByTeamId(teamId);
	}

	public void updateSquad(int id, Player player) {
		Player curr = playerDAO.findById(id);
		curr.copyValue(player);
		playerDAO.updateSquad(curr);
	}
	
	public Player findById(int id) {
		return playerDAO.findById(id);
	}
	
}
