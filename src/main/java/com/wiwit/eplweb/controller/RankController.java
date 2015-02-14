package com.wiwit.eplweb.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wiwit.eplweb.model.Rank;
import com.wiwit.eplweb.model.view.RankModelView;
import com.wiwit.eplweb.service.RankService;

@Controller
public class RankController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(RankController.class);

	private RankService rankService;

	@RequestMapping(value = "/ranks", method = RequestMethod.GET)
	public @ResponseBody
	String getLatestRank(Model model) throws JsonGenerationException,
			JsonMappingException, IOException {
		logger.info("GET /ranks");

		List<Rank> ranks = rankService.getLatestRank();

		return generateJson(RankModelView.getModelView(ranks));
	}

	@RequestMapping(value = "/ranks/{weekNumber}", method = RequestMethod.GET)
	public @ResponseBody
	String getSelectedRank(Model model,
			@PathVariable("weekNumber") String weekNumber)
			throws JsonGenerationException, JsonMappingException, IOException {
		logger.info("GET /ranks/" + weekNumber);

		List<Rank> ranks = rankService.getRankByWeekNumber(weekNumber);

		return generateJson(RankModelView.getModelView(ranks));
	}

	@Autowired(required = true)
	public void setRankService(RankService rankService) {
		this.rankService = rankService;
	}
}
