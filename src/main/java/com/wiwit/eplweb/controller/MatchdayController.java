package com.wiwit.eplweb.controller;

import java.io.IOException;

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

import com.wiwit.eplweb.service.MatchdayService;

@Controller
public class MatchdayController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(MatchdayController.class);

	private MatchdayService matchdayService;

	@RequestMapping(value = "/matchday", method = RequestMethod.GET)
	public @ResponseBody
	String getCurrentMatchday(Model model) throws JsonGenerationException,
			JsonMappingException, IOException {
		logger.info("GET /matchday");

		return generateJson(matchdayService.getMatchtdayOnCurrWeek());
	}

	@RequestMapping(value = "/matchday/{weekNumber}", method = RequestMethod.GET)
	public @ResponseBody
	String getSelectedMatchday(Model model,
			@PathVariable("weekNumber") int weekNumber)
			throws JsonGenerationException, JsonMappingException, IOException {
		logger.info("GET /matchday/" + weekNumber);

		return generateJson(matchdayService.getMatchtdayByWeekNmr(weekNumber));
	}

	@Autowired(required = true)
	public void setMatchdayService(MatchdayService matchdayService) {
		this.matchdayService = matchdayService;
	}
}
