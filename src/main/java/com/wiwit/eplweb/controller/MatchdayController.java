package com.wiwit.eplweb.controller;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wiwit.eplweb.model.view.MatchdayModelView;
import com.wiwit.eplweb.service.MatchdayService;
import com.wiwit.eplweb.util.ApiPath;

@RestController
public class MatchdayController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(MatchdayController.class);

	private MatchdayService matchdayService;

	@RequestMapping(value = ApiPath.MATCHDAYS, method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public MatchdayModelView getCurrentMatchday()
			throws JsonGenerationException, JsonMappingException, IOException {
		logger.info("GET /matchday");

		return matchdayService.getMatchtdayOnCurrWeek();
	}

	@RequestMapping(value = ApiPath.MATCHDAYS_BY_WEEK, method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public MatchdayModelView getSelectedMatchday(
			@PathVariable("weekNumber") int weekNumber)
			throws JsonGenerationException, JsonMappingException, IOException {
		logger.info("GET /matchday/" + weekNumber);

		return matchdayService.getMatchtdayByWeekNmr(weekNumber);
	}

	@Autowired(required = true)
	public void setMatchdayService(MatchdayService matchdayService) {
		this.matchdayService = matchdayService;
	}
}
