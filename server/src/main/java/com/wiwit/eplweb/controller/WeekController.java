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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wiwit.eplweb.model.Week;
import com.wiwit.eplweb.model.view.WeekModelView;
import com.wiwit.eplweb.service.WeekService;
import com.wiwit.eplweb.util.ApiPath;

@RestController
public class WeekController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(WeekController.class);

	@Autowired
	private WeekService weekService;

	// Get all passed week
	@RequestMapping(value = ApiPath.PASSED_WEEK, method = RequestMethod.GET, produces = CONTENT_TYPE_JSON)
	public ResponseEntity<WeekModelView> getAllPassedWeek() throws JsonGenerationException,
			JsonMappingException, IOException {
		logger.info("GET /api/passedWeeks");

		List<Week> weeks = weekService.findAllPassedWeek();
		
		WeekModelView result =  WeekModelView.getModelView(weeks);
		return new ResponseEntity<WeekModelView>(result, HttpStatus.OK);
	}
	
	// Get all weeks
	@RequestMapping(value = ApiPath.WEEKS, method = RequestMethod.GET, produces = CONTENT_TYPE_JSON)
	public ResponseEntity<WeekModelView> getAllWeek() throws JsonGenerationException,
			JsonMappingException, IOException {
		logger.info("GET /api/weeks");

		List<Week> weeks = weekService.getAllWeek();
		
		WeekModelView result =  WeekModelView.getModelView(weeks);
		return new ResponseEntity<WeekModelView>(result, HttpStatus.OK);
	}
}
