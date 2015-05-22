package com.wiwit.eplweb.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.wiwit.eplweb.model.CommentPoint;
import com.wiwit.eplweb.model.Matchday;
import com.wiwit.eplweb.model.MatchdayComment;
import com.wiwit.eplweb.model.User;
import com.wiwit.eplweb.model.input.CommentPointModelInput;
import com.wiwit.eplweb.model.input.MatchdayCommentModelInput;
import com.wiwit.eplweb.model.view.MatchdayCommentModelView;
import com.wiwit.eplweb.service.CommentPointService;
import com.wiwit.eplweb.service.MatchdayCommentService;
import com.wiwit.eplweb.service.MatchdayService;
import com.wiwit.eplweb.util.ApiPath;

@RestController
public class MatchdayCommentController extends BaseController {
	private static final Logger logger = LoggerFactory
			.getLogger(MatchdayCommentController.class);

	private static int TOTAL_COMMENT_LOAD = 5;
	private static int TOTAL_SUBCOMMANT_FIRST_LOAD = 2;
	private static int TOTAL_SUBCOMMENT_NEXT_LOAD = 5;

	@Autowired
	private MatchdayCommentService commentService;

	@Autowired
	private MatchdayService matchdayService;

	@Autowired
	private CommentPointService pointService;

	// Retrieve user changes point of a comment. 
	// The value is "up" or "down". 
	// The comment with most "up" point will be placed on the first load.
	@RequestMapping(value = ApiPath.MATCHDAY_COMMENT_POINT, method = RequestMethod.POST, produces = CONTENT_TYPE_JSON, consumes = CONTENT_TYPE_JSON)
	public ResponseEntity<CommentPoint> postPoint(@PathVariable("commentId") int commentId,
			@RequestBody CommentPointModelInput model, HttpServletRequest req) {
		logger.info("POST /api/matchday/comment/" + commentId + "/point ");

		// Find the user
		int sessionId = (Integer) req.getAttribute(CustomFilter.SESSION_ID);
		User user = getUser(sessionId);

		// Find the comment
		MatchdayComment comment = commentService.findById(commentId);
		if (comment == null) {
			return new ResponseEntity<CommentPoint>(HttpStatus.BAD_REQUEST);
		}

		Boolean latestValue = null;
		// Find the point, when user already submited before.
		CommentPoint point = pointService.findByCommentIdAndUser(
				comment.getId(), user.getId());
		
		if (point == null) {
			// Create new comment point
			point = new CommentPoint();
			
			point.setMatchdayComment(comment);
			point.setUser(user);
		} else {
			// Change the last value
			latestValue = point.getIsUp();
		}

		point.setIsUp(model.isUp());
		pointService.updatePoint(point, latestValue);
		return new ResponseEntity<CommentPoint>(point, HttpStatus.OK);
	}

	// User post new comment
	@RequestMapping(value = ApiPath.MATCHDAY_COMMENTS_BY_MATCH, method = RequestMethod.POST, produces = CONTENT_TYPE_JSON, consumes = CONTENT_TYPE_JSON)
	public ResponseEntity<MatchdayComment> postNewComment(
			@PathVariable("matchdayId") int matchdayId,
			@RequestBody MatchdayCommentModelInput model, HttpServletRequest req) {
		logger.info("POST /api/matchday/" + matchdayId + "/comment");

		// Check the comment validation.
		if (!model.isValid()) {
			return new ResponseEntity<MatchdayComment>(HttpStatus.BAD_REQUEST);
		}

		// Find matchay
		Matchday m = matchdayService.findMatchtdayById(matchdayId);
		if (m == null) {
			return new ResponseEntity<MatchdayComment>(HttpStatus.BAD_REQUEST);
		}

		MatchdayComment entity = new MatchdayComment();
		MatchdayComment parent = null;
		if (model.getParentId() > 0) {
			// If has parentId value, that's mean the user replied a comment.
			// Check the parent comment.
			parent = commentService.findById(model.getParentId());
			if (parent == null)
				return new ResponseEntity<MatchdayComment>(
						HttpStatus.BAD_REQUEST);
			else
				entity.setParent(parent);
		}

		int sessionId = (Integer) req.getAttribute(CustomFilter.SESSION_ID);

		// Set up the new comment.
		entity.setValue(model.getValue());
		entity.setUser(getUser(sessionId));
		entity.setMatchday(m);
		entity.setCreated(new Date());
		entity.setPoints(0);

		// Save the new comment.
		commentService.createComment(entity);
		return new ResponseEntity<MatchdayComment>(entity, HttpStatus.OK);
	}

	// When the user open up the comment section, 
	//  just some sub/children comment with highest "up" point will be loaded.
	// And this function will load more sub/children comemnt when the user click "load" button.
	@RequestMapping(value = ApiPath.MATCHDAY_COMMENTS_BY_PARENT, method = RequestMethod.GET, produces = CONTENT_TYPE_JSON)
	public ResponseEntity<MatchdayCommentModelView> getSubComments(
			@PathVariable("commentId") int commentId, HttpServletRequest req) {
		logger.info("GET /api/matchday/comment/" + commentId
				+ "/loadsubcomment");

		// Validate the offset value
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
		logger.info("GET /api/matchday/comment/" + commentId
				+ "/loadsubcomment?offset=" + offsetInt);

		// Find the sub/children comment base on parent commentId
		MatchdayCommentModelView result = new MatchdayCommentModelView();
		result.setComments(commentService.findByParentId(commentId, offsetInt,
				TOTAL_SUBCOMMENT_NEXT_LOAD));
		result.setTotalComment(commentService
				.countTotalCommentByMatchdayId(commentId));

		return new ResponseEntity<MatchdayCommentModelView>(result,
				HttpStatus.OK);
	}

	// Same as the above method, but its load the parent comemnt.
	@RequestMapping(value = ApiPath.MATCHDAY_COMMENTS_BY_MATCH, method = RequestMethod.GET, produces = CONTENT_TYPE_JSON)
	public ResponseEntity<MatchdayCommentModelView> getComments(
			@PathVariable("matchdayId") int matchdayId, HttpServletRequest req) {
		logger.info("GET /api/matchday/" + matchdayId + "/comment");

		// Validate the offset value
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
		result.setComments(commentService.findByMatchdayId(matchdayId,
				offsetInt, TOTAL_COMMENT_LOAD));

		// The section above will find some top comment 
		//  and get user comment on current match when the user has been post comment.
		// It's ok maybe we would send redundant comments when the user comment place on
		//  top comment. because the client would divide it.

		// Get all comment
		result.setTotalComment(commentService
				.countTotalCommentByMatchdayId(matchdayId));
		for (MatchdayComment comment : result.getComments()) {
			comment.setSubComment(commentService.findByParentId(
					comment.getId(), 0, TOTAL_SUBCOMMANT_FIRST_LOAD));
			comment.setTotalSubComment(commentService
					.countTotalCommentByParentId(comment.getId()));
		}
		// User comments
		if (req.getAttribute(CustomFilter.SESSION_ID) != null) {
			int sessionId = (Integer) req.getAttribute(CustomFilter.SESSION_ID);
			User user = getUser(sessionId); 
			result.setMyComments(commentService.findByMatchAndUser(matchdayId,
					user, 0, TOTAL_SUBCOMMANT_FIRST_LOAD));
			for (MatchdayComment comment : result.getMyComments()) {
				comment.setSubComment(commentService.findByParentId(
						comment.getId(), 0, TOTAL_SUBCOMMANT_FIRST_LOAD));
				comment.setTotalSubComment(commentService
						.countTotalCommentByParentId(comment.getId()));
			}
			// If offset == 0, its mean this request is the first fetch request, 
			//   need to set List of CommentPoint
			if (0 == offsetInt) {
				List<CommentPoint> myPoints = pointService.findByMatchIdAndUserId(matchdayId, user.getId());
				result.setMyPoints(myPoints);
			}
		}
		

		return new ResponseEntity<MatchdayCommentModelView>(result,
				HttpStatus.OK);
	}
}
