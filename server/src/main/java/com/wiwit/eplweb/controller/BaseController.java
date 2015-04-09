package com.wiwit.eplweb.controller;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;

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
}
