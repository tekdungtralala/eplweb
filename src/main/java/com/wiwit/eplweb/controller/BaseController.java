package com.wiwit.eplweb.controller;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class BaseController {

	public String generateJson(Object result) throws JsonGenerationException, JsonMappingException, IOException{
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		return ow.writeValueAsString(result);
	}
	
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public class ResourceNotFoundException extends RuntimeException {
	}
	
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public class ResourceBadRequestExceptidon extends RuntimeException {
	}
	
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public class ResourceInternalErrorExceptidon extends RuntimeException {
	}
	
	@ResponseStatus(value = HttpStatus.FORBIDDEN)
	public class ResourceForbiddenExceptidon extends RuntimeException {
	}
	
	public void throw400(){
		throw new ResourceBadRequestExceptidon();
	}
	
	public void throw403(){
		throw new ResourceForbiddenExceptidon();
	}
	
	public void throw404(){
		throw new ResourceNotFoundException();
	}
	
	public void throw500(){
		throw new ResourceInternalErrorExceptidon();
	}

}
