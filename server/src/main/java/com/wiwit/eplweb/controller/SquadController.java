package com.wiwit.eplweb.controller;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wiwit.eplweb.model.Player;
import com.wiwit.eplweb.model.view.SimpleResult;
import com.wiwit.eplweb.service.PlayerService;
import com.wiwit.eplweb.util.ApiPath;
import com.wiwit.eplweb.util.RestResult;

@RestController
public class SquadController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(SquadController.class);

	@Autowired
	public PlayerService playerService;
	
	// Delete player
	@RequestMapping(value = ApiPath.SQUAD_BY_ID, method = RequestMethod.DELETE)
	public ResponseEntity<String> deletePlayer(@PathVariable("playerId") int playerId) {
		logger.info("DELETE /api/players/" + playerId);
		
		Player p = playerService.findById(playerId);
		
		if (p == null) {
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		} else {
			playerService.deletePlayer(p);
			return new ResponseEntity<String>(HttpStatus.OK);
		}
	}

	// Create new player
	@RequestMapping(value = ApiPath.SQUAD, method = RequestMethod.POST, consumes = CONTENT_TYPE_JSON)
	public ResponseEntity<Object> postPlayer(@RequestBody final Player player)
			throws JsonGenerationException, JsonMappingException, IOException {
		logger.info("POST /api/players");

		int teamId = player.getTeam().getId();
		int playerNumber = player.getPlayerNumber();

		// Find player on team by playerNumber
		Player p = playerService.findByTeamAndNumber(teamId, playerNumber);

		if (p != null) {
			// Can't create new player on a team with same playerNumber
			String errorMsg = "Player with player number = " + playerNumber
					+ " on team id = " + teamId + " already exist!";

			RestResult er = new RestResult(HttpStatus.CONFLICT.value(),
					errorMsg);

			return new ResponseEntity<Object>(er, HttpStatus.CONFLICT);
		} else {
			// Save new player
			playerService.savePlayer(player);
			return new ResponseEntity<Object>(HttpStatus.OK);
		}
	}

	// Edit player
	@RequestMapping(value = ApiPath.SQUAD_BY_ID, method = RequestMethod.PUT, consumes = CONTENT_TYPE_JSON)
	public ResponseEntity putPlayer(@PathVariable("playerId") int playerId,
			@RequestBody final Player player) {
		logger.info("PUT /api/players/" + playerId);

		playerService.updatePlayer(playerId, player);
		return new ResponseEntity(HttpStatus.OK);
	}

	// Get all players on a team by teamId
	@RequestMapping(value = ApiPath.SQUADS_BY_TEAM, method = RequestMethod.GET, produces = CONTENT_TYPE_JSON)
	public ResponseEntity<SimpleResult> getFiveHighestRank(@PathVariable("teemId") int teamId)
			throws JsonGenerationException, JsonMappingException, IOException {
		logger.info("GET /api/players/team/" + teamId);

		List<Player> result = playerService.getSquadsByTeamId(teamId);

		return new ResponseEntity(SimpleResult.generateResult(result),HttpStatus.OK);
	}
}
