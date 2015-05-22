package com.wiwit.eplweb.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
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

import com.wiwit.eplweb.model.User;
import com.wiwit.eplweb.model.UserSession;
import com.wiwit.eplweb.model.view.SimpleResult;
import com.wiwit.eplweb.service.UserService;
import com.wiwit.eplweb.service.UserSessionService;
import com.wiwit.eplweb.util.ApiPath;

@RestController
public class AdminController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(AdminController.class);

	@Autowired
	private UserService userService;
	@Autowired
	private UserSessionService sessionService;
	
	// Remove user session
	@RequestMapping(value = ApiPath.ADMIN_SESSION, method = RequestMethod.DELETE, produces = CONTENT_TYPE_JSON)
	public ResponseEntity<String> removeSession(@PathVariable("session") String session)
			throws JsonGenerationException, JsonMappingException, IOException {
		logger.info("DELETE /api/admin/login/" + session);

		sessionService.deleteSession(session);

		return new ResponseEntity<String>(HttpStatus.OK);
	}

	// Find user session, return 404 when not found
	@RequestMapping(value = ApiPath.ADMIN_SESSION, method = RequestMethod.GET, produces = CONTENT_TYPE_JSON)
	public ResponseEntity<SimpleResult> checkSession(@PathVariable("session") String session)
			throws JsonGenerationException, JsonMappingException, IOException {
		logger.info("GET /api/admin/login/" + session);

		UserSession us = sessionService.findBySession(session);

		if (us == null)
			return new ResponseEntity<SimpleResult>(HttpStatus.NOT_FOUND);

		return new ResponseEntity<SimpleResult>(SimpleResult.generateResult(us), HttpStatus.OK);
	}

	// Simple Admin login, retrive email and password
	// Next time we must upgrade this section
	@RequestMapping(value = ApiPath.ADMIN_LOGIN, method = RequestMethod.POST, produces = CONTENT_TYPE_JSON)
	public ResponseEntity<SimpleResult> createSession(HttpServletRequest request)
			throws JsonGenerationException, JsonMappingException, IOException {
		logger.info("POST /api/admin/login");

		String adminEmailEncode = request.getParameter("adminEmailEncode");
		String adminPaswdEncode = request.getParameter("adminPaswdEncode");

		logger.info("adminEmailEncode : " + adminEmailEncode);
		logger.info("adminPaswdEncode : " + adminPaswdEncode);

		if (adminEmailEncode == null || adminEmailEncode.isEmpty()
				|| adminPaswdEncode == null || adminPaswdEncode.isEmpty()) {
			return new ResponseEntity<SimpleResult>(HttpStatus.NOT_FOUND);
		}
		byte[] decodedBytes = Base64.decodeBase64(adminEmailEncode);
		String decodedEmail = new String(decodedBytes);

		User u = userService.findUserByEmail(decodedEmail);

		if (u == null)
			return new ResponseEntity<SimpleResult>(HttpStatus.NOT_FOUND);

		if (u.getPassword().equals(adminPaswdEncode)) {
			UserSession session = sessionService.doLogin(u);
			return new ResponseEntity<SimpleResult>(SimpleResult.generateResult(session), HttpStatus.OK);
		}

		return new ResponseEntity<SimpleResult>(HttpStatus.NOT_FOUND);
	}
}
