package com.wiwit.eplweb.controller;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wiwit.eplweb.model.Rank;
import com.wiwit.eplweb.model.view.RankModelView;
import com.wiwit.eplweb.service.RankService;
import com.wiwit.eplweb.util.ApiPath;

@RestController
public class RankController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(RankController.class);

	private RankService rankService;

	@RequestMapping(value = ApiPath.HIGHEST_RANK, method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public RankModelView getFiveHighestRank() throws JsonGenerationException,
			JsonMappingException, IOException {
		logger.info("GET /api/highestRanks");

		List<Rank> ranks = rankService.getFiveHighestLastRank();

		return RankModelView.getModelView(ranks);
	}

	@RequestMapping(value = ApiPath.RANKS, method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public RankModelView getLatestRank() throws JsonGenerationException,
			JsonMappingException, IOException {
		logger.info("GET /api/ranks");

		List<Rank> ranks = rankService.getLatestRank();

		return RankModelView.getModelView(ranks);
	}

	@RequestMapping(value = ApiPath.RANKS_BY_WEEK, method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public RankModelView getSelectedRank(
			@PathVariable("weekNumber") int weekNumber)
			throws JsonGenerationException, JsonMappingException, IOException {
		logger.info("GET /api/ranks/" + weekNumber);

		List<Rank> ranks = rankService.getRankByWeekNumber(weekNumber);

		return RankModelView.getModelView(ranks);
	}

	@Autowired(required = true)
	public void setRankService(RankService rankService) {
		this.rankService = rankService;
	}
}
