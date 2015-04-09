package com.wiwit.eplweb.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

import com.wiwit.eplweb.filter.CustomFilter;
import com.wiwit.eplweb.model.input.MatchdayModelInput;
import com.wiwit.eplweb.model.input.RatingModelInput;
import com.wiwit.eplweb.model.input.ScoreModelInput;
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

		MatchdayModelView result =  matchdayService.findMatchtdayOnCurrWeek();
		return new ResponseEntity<MatchdayModelView>(result, HttpStatus.OK);
	}

	@RequestMapping(value = ApiPath.MATCHDAYS_BY_WEEK, method = RequestMethod.GET, produces = CONTENT_TYPE_JSON)
	public ResponseEntity<MatchdayModelView> getSelectedMatchday(
			@PathVariable("weekNumber") int weekNumber)
			throws JsonGenerationException, JsonMappingException, IOException {
		logger.info("GET /matchday/" + weekNumber);

		MatchdayModelView result =  matchdayService.findMatchtdayByWeekNmr(weekNumber);
		return new ResponseEntity<MatchdayModelView>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value = ApiPath.MATCHDAYS_CHANGE_RATING, method = RequestMethod.POST, consumes = CONTENT_TYPE_JSON)
	public void updateRATING(@PathVariable("matchdayId") int matchdayId,
			HttpServletRequest req, @RequestBody RatingModelInput rating) {
		logger.info("PUT /api/matchday/" + matchdayId + "/updateRating");
		
		int sessionId = (Integer) req.getAttribute(CustomFilter.SESSION_ID);
		logger.info("sessionId: " + sessionId);
		logger.info("userId: " + getUser(sessionId).getId());
		
//		logger.info("rating : " + rating.getRating());
//		logger.info("sessionId : " + req.getAttribute("sessionId"));
		
		
//		matchdayService.updateScore(matchdayId, updateScore);
	}
	
	@RequestMapping(value = ApiPath.MATCHDAYS_CHANGE_SCORE, method = RequestMethod.PUT, consumes = CONTENT_TYPE_JSON)
	public void updateScore(@PathVariable("matchdayId") int matchdayId,
			@RequestBody ScoreModelInput updateScore) {
		logger.info("PUT /api/matchday/" + matchdayId + "/updateScore");
		
		matchdayService.updateScore(matchdayId, updateScore);
	}
	
	@RequestMapping(value = ApiPath.MATCHDAYS_CHANGE_SCHEDULE, method = RequestMethod.POST, consumes = CONTENT_TYPE_JSON)
	public void updateMatchdays(@PathVariable("weekNumber") int weekNumber,
			@RequestBody List<MatchdayModelInput> matchs) {
		logger.info("POST /api/updateMatchday/" + weekNumber);
		
		matchdayService.updateMatchdays(weekNumber, matchs);
	}
}