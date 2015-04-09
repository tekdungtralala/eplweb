package com.wiwit.eplweb.controller;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.wiwit.eplweb.model.User;
import com.wiwit.eplweb.model.UserSession;
import com.wiwit.eplweb.model.view.SimpleResult;
import com.wiwit.eplweb.service.UserSessionService;

public class BaseController {

	public static final String CONTENT_TYPE_JSON = "application/json";
	
	@Autowired
	protected UserSessionService userSessionService;
	public User getUser(int id) {
		UserSession us = userSessionService.findById(id); 
		if (us.getUser() != null) return us.getUser();
		
		return us.getUserNetwork().getUser();
		
	}

	public String generateJson(Object result) throws JsonGenerationException,
			JsonMappingException, IOException {
		ObjectWriter ow = new ObjectMapper().writer()
				.withDefaultPrettyPrinter();
		return ow.writeValueAsString(result);
	}

	public String generateSimpleResult(Object result)
			throws JsonGenerationException, JsonMappingException, IOException {
		ObjectWriter ow = new ObjectMapper().writer()
				.withDefaultPrettyPrinter();
		SimpleResult sr = SimpleResult.generateResult(result);
		return ow.writeValueAsString(sr);
	}

	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public class ResourceNotFoundException extends RuntimeException {
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public class ResourceBadRequestException extends RuntimeException {
	}

	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public class ResourceInternalErrorException extends RuntimeException {
	}

	@ResponseStatus(value = HttpStatus.FORBIDDEN)
	public class ResourceForbiddenException extends RuntimeException {
	}

	@ResponseStatus(value = HttpStatus.CONFLICT)
	public class ResourceConflictException extends RuntimeException {
		public ResourceConflictException(String message) {
			super(message);
		}

	}

	public void throw400() {
		throw new ResourceBadRequestException();
	}

	public void throw403() {
		throw new ResourceForbiddenException();
	}

	public void throw404() {
		throw new ResourceNotFoundException();
	}

	public void throw500() {
		throw new ResourceInternalErrorException();
	}

	public void throw409(String message) {
		throw new ResourceConflictException(message);
	}
}
