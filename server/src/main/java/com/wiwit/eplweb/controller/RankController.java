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

import com.wiwit.eplweb.model.Rank;
import com.wiwit.eplweb.model.input.RankModelInput;
import com.wiwit.eplweb.model.view.RankModelView;
import com.wiwit.eplweb.service.RankService;
import com.wiwit.eplweb.util.ApiPath;

@RestController
public class RankController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(RankController.class);

	@Autowired
	private RankService rankService;

	// Get 5 highest rank on current week
	@RequestMapping(value = ApiPath.HIGHEST_RANK, method = RequestMethod.GET, produces = CONTENT_TYPE_JSON)
	public ResponseEntity<RankModelView> getFiveHighestRank() throws JsonGenerationException,
			JsonMappingException, IOException {
		logger.info("GET /api/highestRanks");

		List<Rank> ranks = rankService.getFiveHighestLastRank();

		return new ResponseEntity<RankModelView>(RankModelView.getModelView(ranks), HttpStatus.OK);
	}

	// Get ranks on cureent week
	@RequestMapping(value = ApiPath.RANKS, method = RequestMethod.GET, produces = CONTENT_TYPE_JSON)
	public ResponseEntity<RankModelView> getLatestRank() throws JsonGenerationException,
			JsonMappingException, IOException {
		logger.info("GET /api/ranks");

		List<Rank> ranks = rankService.findLatestRank();

		return new ResponseEntity<RankModelView>(RankModelView.getModelView(ranks), HttpStatus.OK);
	}

	// Get ranks by weeknumber
	@RequestMapping(value = ApiPath.RANKS_BY_WEEK, method = RequestMethod.GET, produces = CONTENT_TYPE_JSON)
	public ResponseEntity<RankModelView> getSelectedRank(
			@PathVariable("weekNumber") int weekNumber)
			throws JsonGenerationException, JsonMappingException, IOException {
		logger.info("GET /api/ranks/" + weekNumber);

		List<Rank> ranks = rankService.findRankByWeekNumber(weekNumber);

		return new ResponseEntity<RankModelView>(RankModelView.getModelView(ranks), HttpStatus.OK);
	}
	
	// After user update score of matchs on a week, the ranks not automaticaly updated.
	// Need to update manualy with weeknumber value.
	// Example, if it's given 17 on weeknumber then from week 1-17will be count 
	//  the number of win, lose, draw and a goal difference of each team.
	//  And the final result is week 1 - 17 will have a new ranking position.
	@RequestMapping(value = ApiPath.UPDATE_RANK, method = RequestMethod.POST, consumes = CONTENT_TYPE_JSON)
	public ResponseEntity updateRank(@RequestBody RankModelInput ur) {
		logger.info("POST /api/updateRanks weekNumber=" + ur.getWeekNumber());
		
		rankService.updateRanking(1, ur.getWeekNumber());
		return new ResponseEntity(HttpStatus.OK);
	}
}
