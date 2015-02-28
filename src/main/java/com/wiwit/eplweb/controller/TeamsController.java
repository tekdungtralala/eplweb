package com.wiwit.eplweb.controller;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wiwit.eplweb.model.Team;
import com.wiwit.eplweb.service.TeamService;

@Controller
public class TeamsController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(TeamsController.class);

	public TeamService teamService;

	@RequestMapping(value = "/api/teams", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public @ResponseBody
	String getFiveHighestRank() throws JsonGenerationException,
			JsonMappingException, IOException {
		logger.info("GET /api/teams");

		List<Team> result = teamService.findAll();

		return generateSimpleResult(result);
	}

	@Autowired(required = true)
	public void setTeamService(TeamService teamService) {
		this.teamService = teamService;
	}
}
