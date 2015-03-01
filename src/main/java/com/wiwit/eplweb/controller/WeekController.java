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

import com.wiwit.eplweb.model.Week;
import com.wiwit.eplweb.model.view.WeekModelView;
import com.wiwit.eplweb.service.WeekService;

@Controller
public class WeekController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(WeekController.class);

	private WeekService weekService;

	@RequestMapping(value = "/api/passedWeeks", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public @ResponseBody
	String getAllPassedWeek() throws JsonGenerationException,
			JsonMappingException, IOException {
		logger.info("GET /api/passedWeeks");

		List<Week> weeks = weekService.getAllPassedWeek();
		return generateJson(WeekModelView.getModelView(weeks));
	}
	
	@RequestMapping(value = "/api/weeks", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public @ResponseBody
	String getAllWeek() throws JsonGenerationException,
			JsonMappingException, IOException {
		logger.info("GET /api/weeks");

		List<Week> weeks = weekService.getAllWeek();
		return generateJson(WeekModelView.getModelView(weeks));
	}

	@Autowired(required = true)
	public void setWeekService(WeekService weekService) {
		this.weekService = weekService;
	}
}
