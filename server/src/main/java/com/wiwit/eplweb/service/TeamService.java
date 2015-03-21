package com.wiwit.eplweb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wiwit.eplweb.dao.TeamDAO;
import com.wiwit.eplweb.model.Team;

@Component
public class TeamService {

	@Autowired
	private TeamDAO teamDAO;

	public List<Team> findAll() {
		return teamDAO.findAll();
	}

	public Team findByIdAndSimppleName(int id, String simpleName) {
		return teamDAO.findByIdAndName(id, simpleName);
	}

	public void updateTeam(int id, Team team) {
		Team t = teamDAO.findById(id);
		t.copyValue(team);
		teamDAO.updateTeam(t);
	}

	public Team findById(int teamId) {
		return teamDAO.findById(teamId);
	}
}
