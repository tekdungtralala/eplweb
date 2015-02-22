package com.wiwit.eplweb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wiwit.eplweb.dao.UserSessionDAO;
import com.wiwit.eplweb.model.UserSession;

@Component
public class UserSessionService {

	@Autowired
	private UserSessionDAO userSessionDAO;

	public List<UserSession> findAll() {
		return userSessionDAO.findAll();
	}
}
