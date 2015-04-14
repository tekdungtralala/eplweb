package com.wiwit.eplweb.controller;

import java.io.IOException;
import java.util.ArrayList;
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
import com.wiwit.eplweb.model.Matchday;
import com.wiwit.eplweb.model.MatchdayVoting;
import com.wiwit.eplweb.model.User;
import com.wiwit.eplweb.model.Week;
import com.wiwit.eplweb.model.input.MatchdayModelInput;
import com.wiwit.eplweb.model.input.RatingModelInput;
import com.wiwit.eplweb.model.input.ScoreModelInput;
import com.wiwit.eplweb.model.input.VotingModelInput;
import com.wiwit.eplweb.model.view.MatchdayModelView;
import com.wiwit.eplweb.service.MatchdayService;
import com.wiwit.eplweb.service.MatchdayVotingService;
import com.wiwit.eplweb.service.WeekService;
import com.wiwit.eplweb.util.ApiPath;
import com.wiwit.eplweb.util.VoteType;

@RestController
public class MatchdayController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(MatchdayController.class);

	@Autowired
	private MatchdayService matchdayService;
	
	@Autowired
	private WeekService weekService;
	
	@Autowired
	private MatchdayVotingService votingService;

	@RequestMapping(value = ApiPath.MATCHDAYS, method = RequestMethod.GET, produces = CONTENT_TYPE_JSON)
	public ResponseEntity<MatchdayModelView> getCurrentMatchday()
			throws JsonGenerationException, JsonMappingException, IOException {
		logger.info("GET /matchday");

		MatchdayModelView result =  matchdayService.findMatchtdayOnCurrWeek();
		return new ResponseEntity<MatchdayModelView>(result, HttpStatus.OK);
	}

	@RequestMapping(value = ApiPath.MATCHDAYS_BY_WEEK, method = RequestMethod.GET, produces = CONTENT_TYPE_JSON)
	public ResponseEntity<MatchdayModelView> getSelectedMatchday(
			@PathVariable("weekNumber") int weekNumber, HttpServletRequest req) {
		logger.info("GET /matchday/" + weekNumber);
		
		Week week = weekService.findByWeekNumber(weekNumber);
		if (week == null) return new ResponseEntity(HttpStatus.NOT_FOUND);
		
		List<Matchday> listMatchday = matchdayService.findMatchtdayByWeekNumber(weekNumber);
		MatchdayModelView result = new MatchdayModelView(listMatchday, week);
		
		User user = null;
		Integer sessionId = (Integer) req.getAttribute(CustomFilter.SESSION_ID);
		if (sessionId != null) {
			user = getUser(sessionId);
			
			List<Integer> ids = new ArrayList<Integer>();
			for(Matchday m : listMatchday) {
				ids.add(m.getId());
			}
			List<MatchdayVoting> votings = votingService.findByMatchdayIdsAndUser(ids, user);
			
			result.setVotings(votings);
		}
		
		return new ResponseEntity<MatchdayModelView>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value = ApiPath.MATCHDAYS_CHANGE_RATING, method = RequestMethod.POST, consumes = CONTENT_TYPE_JSON)
	public ResponseEntity<Matchday> updateRating(@PathVariable("matchdayId") int matchdayId,
			HttpServletRequest req, @RequestBody RatingModelInput rating) {
		logger.info("PUT /api/matchday/" + matchdayId + "/updateRating");
		
		int sessionId = (Integer) req.getAttribute(CustomFilter.SESSION_ID);
		
		Matchday match = matchdayService.findMatchtdayById(matchdayId);
		if (match == null) return new ResponseEntity(HttpStatus.NOT_FOUND);		
		
		matchdayService.updateRating(match, getUser(sessionId), rating.getRating());
		return new ResponseEntity<Matchday>(match, HttpStatus.OK);
	}
	
	@RequestMapping(value = ApiPath.MATCHDAYS_CHANGE_VOTING, method = RequestMethod.POST, consumes = CONTENT_TYPE_JSON)
	public ResponseEntity<Matchday> updateVoting(@PathVariable("matchdayId") int matchdayId,
			HttpServletRequest req, @RequestBody VotingModelInput voting) {
		logger.info("PUT /api/matchday/" + matchdayId + "/updateVoting");
		
		if (!voting.isValid())
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		
		int sessionId = (Integer) req.getAttribute(CustomFilter.SESSION_ID);
		
		Matchday match = matchdayService.findMatchtdayById(matchdayId);
		if (match == null) return new ResponseEntity(HttpStatus.NOT_FOUND);
		
		VoteType vote = VoteType.getVote(voting.getVote());
		
		matchdayService.updateVoting(match, getUser(sessionId), vote);
		return new ResponseEntity<Matchday>(match, HttpStatus.OK);
	}
	
	@RequestMapping(value = ApiPath.MATCHDAYS_CHANGE_SCORE, method = RequestMethod.PUT, consumes = CONTENT_TYPE_JSON)
	public ResponseEntity updateScore(@PathVariable("matchdayId") int matchdayId,
			@RequestBody ScoreModelInput updateScore) {
		logger.info("PUT /api/matchday/" + matchdayId + "/updateScore");
		
		matchdayService.updateScore(matchdayId, updateScore);
		
		return new ResponseEntity(HttpStatus.OK);
	}
	
	@RequestMapping(value = ApiPath.MATCHDAYS_CHANGE_SCHEDULE, method = RequestMethod.POST, consumes = CONTENT_TYPE_JSON)
	public ResponseEntity updateMatchdays(@PathVariable("weekNumber") int weekNumber,
			@RequestBody List<MatchdayModelInput> matchs) {
		logger.info("POST /api/updateMatchday/" + weekNumber);
		
		matchdayService.updateMatchdays(weekNumber, matchs);
		return new ResponseEntity(HttpStatus.OK);
	}
}