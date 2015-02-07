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
	private PlayerDAO squadDAO;
	
	@Transactional
	public List<Player> getSquadsByTeamId(int teamId){
		return squadDAO.getSquadsByTeamId(teamId);
	}
}
