package com.wiwit.eplweb.controller;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.annotate.JsonSerialize;
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
import com.wiwit.eplweb.model.UserNetwork;
import com.wiwit.eplweb.model.UserSession;
import com.wiwit.eplweb.model.input.UserNetworkModelInput;
import com.wiwit.eplweb.model.view.SimpleResult;
import com.wiwit.eplweb.service.UserNetworkService;
import com.wiwit.eplweb.service.UserSessionService;
import com.wiwit.eplweb.util.ApiPath;
import com.wiwit.eplweb.util.UserNetworkType;

@RestController
public class UserController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(UserController.class);

	@Autowired
	private UserNetworkService userService;
	@Autowired
	private UserSessionService sessionService;
	
	@RequestMapping(value = ApiPath.USER_SESSION, method = RequestMethod.DELETE, produces = CONTENT_TYPE_JSON)
	public ResponseEntity<String> removeSession(@PathVariable("session") String session)
			throws JsonGenerationException, JsonMappingException, IOException {
		logger.info("DELETE /api/usernetwork/signin/" + session);

		sessionService.deleteSession(session);

		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@RequestMapping(value = ApiPath.USER_SESSION, method = RequestMethod.GET, produces = CONTENT_TYPE_JSON)
	public ResponseEntity<UserSession> checkSession(@PathVariable("session") String session)
			throws JsonGenerationException, JsonMappingException, IOException {
		logger.info("GET /api/usernetwork/signin/" + session);

		UserSession us = sessionService.findBySession(session);

		if (us == null)
			return new ResponseEntity<UserSession>(HttpStatus.NOT_FOUND);

		return new ResponseEntity<UserSession>(us, HttpStatus.OK);
	}

	@RequestMapping(value = ApiPath.USER_SIGNIN, method = RequestMethod.POST, consumes = CONTENT_TYPE_JSON)
	public ResponseEntity<UserSession> removeSession(
			@RequestBody UserNetworkModelInput model)
			throws JsonGenerationException, JsonMappingException, IOException {
		logger.info("POST /api/usernetwork/signin");

		if (!model.isValidModel())
			return new ResponseEntity<UserSession>(HttpStatus.BAD_REQUEST);

		UserNetworkType type = UserNetworkType.valueOf(model.getType());
		UserNetwork user = userService.findByEmailAndType(model.getEmail(), type);
		
		if (user == null) {
			user = new UserNetwork(model);
			userService.create(user);
		}
		UserSession session = sessionService.doLogin(user);

		return new ResponseEntity<UserSession>(session, HttpStatus.OK);
	}
}
