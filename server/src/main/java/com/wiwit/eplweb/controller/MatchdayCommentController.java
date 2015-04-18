package com.wiwit.eplweb.controller;

import javax.servlet.http.HttpServletRequest;

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

	private static int TOTAL_COMMENT_FIRST_LOAD = 5;
	private static int TOTAL_SUBCOMMANT_FIRST_LOAD = 2;
	private static int TOTAL_COMMENT_NEXT_LOAD = 5;
	private static int TOTAL_SUBCOMMENT_NEXT_LOAD = 5;

	@Autowired
	private MatchdayCommentService commentService;

	@RequestMapping(value = ApiPath.MATCHDAY_COMMENTS_BY_PARENT, method = RequestMethod.GET, produces = CONTENT_TYPE_JSON)
	public ResponseEntity<MatchdayCommentModelView> getSubComments(
			@PathVariable("commentId") int commentId, HttpServletRequest req) {
		logger.info("GET /api/matchday/comment/" + commentId + "/loadsubcomment");

		String offsetStr = req.getParameter("offset");
		int offsetInt = 0;
		if (offsetStr != null) {
			try {
				offsetInt = Integer.valueOf(offsetStr);
			} catch (Exception e) {
				return new ResponseEntity<MatchdayCommentModelView>(
						HttpStatus.BAD_REQUEST);
			}
		}
		logger.info("GET /api/matchday/comment/" + commentId + "/loadsubcomment?offset="+offsetInt);

		MatchdayCommentModelView result = new MatchdayCommentModelView();
		result.setComments(commentService.findByParentId(commentId, offsetInt,
				TOTAL_SUBCOMMENT_NEXT_LOAD));
		result.setTotalComment(commentService
				.countTotalCommentByMatchdayId(commentId));

		return new ResponseEntity<MatchdayCommentModelView>(result,
				HttpStatus.OK);
	}

	@RequestMapping(value = ApiPath.MATCHDAY_COMMENTS_BY_MATCH, method = RequestMethod.GET, produces = CONTENT_TYPE_JSON)
	public ResponseEntity<MatchdayCommentModelView> getComments(
			@PathVariable("matchdayId") int matchdayId, HttpServletRequest req) {
		logger.info("GET /api/matchday/" + matchdayId + "/comment");

		String offsetStr = req.getParameter("offset");
		int offsetInt = 0;
		if (offsetStr != null) {
			try {
				offsetInt = Integer.valueOf(offsetStr);
			} catch (Exception e) {
				return new ResponseEntity<MatchdayCommentModelView>(
						HttpStatus.BAD_REQUEST);
			}
		}

		MatchdayCommentModelView result = new MatchdayCommentModelView();
		result.setComments(commentService.findByMathdayId(matchdayId,
				offsetInt, TOTAL_COMMENT_FIRST_LOAD));
		result.setTotalComment(commentService
				.countTotalCommentByMatchdayId(matchdayId));

		for (MatchdayComment comment : result.getComments()) {
			comment.setSubComment(commentService.findByParentId(
					comment.getId(), 0, TOTAL_SUBCOMMANT_FIRST_LOAD));
			comment.setTotalSubComment(commentService
					.countTotalCommentByParentId(comment.getId()));
		}

		return new ResponseEntity<MatchdayCommentModelView>(result,
				HttpStatus.OK);
	}
}
