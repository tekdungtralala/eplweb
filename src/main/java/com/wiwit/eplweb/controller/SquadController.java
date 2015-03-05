package com.wiwit.eplweb.controller;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wiwit.eplweb.model.Player;
import com.wiwit.eplweb.model.view.SimpleResult;
import com.wiwit.eplweb.service.PlayerService;
import com.wiwit.eplweb.util.ApiPath;

@RestController
public class SquadController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(SquadController.class);

	@Autowired
	public PlayerService playerService;

	@RequestMapping(value = ApiPath.SQUAD, method = RequestMethod.PUT)
	public void putPlayer(@PathVariable("playerId") int playerId, @RequestBody final Player player) {
		logger.info("PUT /api/players/" + playerId);
		
		playerService.updateSquad(playerId, player);
	}

	@RequestMapping(value = ApiPath.SQUADS_BY_TEAM, method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public SimpleResult getFiveHighestRank(@PathVariable("teemId") int teamId)
			throws JsonGenerationException, JsonMappingException, IOException {
		logger.info("GET /api/players/team/" + teamId);

		List<Player> result = playerService.getSquadsByTeamId(teamId);

		return SimpleResult.generateResult(result);
	}
}
