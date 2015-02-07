package com.wiwit.eplweb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wiwit.eplweb.dao.SquadDAO;
import com.wiwit.eplweb.model.Squad;

@Component
public class SquadService {

	@Autowired
	private SquadDAO squadDAO;
	
	@Transactional
	public List<Squad> getSquadsByTeamId(int teamId){
		return squadDAO.getSquadsByTeamId(teamId);
	}
}
