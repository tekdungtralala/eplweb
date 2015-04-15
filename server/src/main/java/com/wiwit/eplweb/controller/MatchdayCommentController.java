package com.wiwit.eplweb.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wiwit.eplweb.model.MatchdayComment;
import com.wiwit.eplweb.model.view.MatchdayCommentModelView;
import com.wiwit.eplweb.service.MatchdayCommentService;
import com.wiwit.eplweb.util.ApiPath;

@RestController
public class MatchdayCommentController extends BaseController {
	private static final Logger logger = LoggerFactory
			.getLogger(MatchdayCommentController.class);

	@Autowired
	private MatchdayCommentService commentService;

	@RequestMapping(value = ApiPath.MATCHDAY_COMMENTS_BY_MATCH, method = RequestMethod.GET, produces = CONTENT_TYPE_JSON)
	public ResponseEntity<MatchdayCommentModelView> getComments(
			@PathVariable("matchdayId") int matchdayId) {
		logger.info("GET /api/matchday/" + matchdayId + "/comment");

		MatchdayCommentModelView result = new MatchdayCommentModelView();
		result.setComments(commentService.findByMathdayId(matchdayId));

		for (MatchdayComment comment : result.getComments()) {
			comment.setSubComment(commentService.findByParentId(comment.getId(), 0, 3));
		}

		return new ResponseEntity<MatchdayCommentModelView>(result,
				HttpStatus.OK);
	}
}
