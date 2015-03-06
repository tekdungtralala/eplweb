package com.wiwit.eplweb.controller;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

	@Autowired
	private MatchdayService matchdayService;

	@RequestMapping(value = ApiPath.MATCHDAYS, method = RequestMethod.GET, produces = CONTENT_TYPE_JSON)
	public ResponseEntity<MatchdayModelView> getCurrentMatchday()
			throws JsonGenerationException, JsonMappingException, IOException {
		logger.info("GET /matchday");

		MatchdayModelView result =  matchdayService.getMatchtdayOnCurrWeek();
		return new ResponseEntity<MatchdayModelView>(result, HttpStatus.OK);
	}

	@RequestMapping(value = ApiPath.MATCHDAYS_BY_WEEK, method = RequestMethod.GET, produces = CONTENT_TYPE_JSON)
	public ResponseEntity<MatchdayModelView> getSelectedMatchday(
			@PathVariable("weekNumber") int weekNumber)
			throws JsonGenerationException, JsonMappingException, IOException {
		logger.info("GET /matchday/" + weekNumber);

		MatchdayModelView result =  matchdayService.getMatchtdayByWeekNmr(weekNumber);
		return new ResponseEntity<MatchdayModelView>(result, HttpStatus.OK);
	}
}
