package com.wiwit.eplweb.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wiwit.eplweb.dao.UserSessionDAO;
import com.wiwit.eplweb.model.User;
import com.wiwit.eplweb.model.UserNetwork;
import com.wiwit.eplweb.model.UserSession;

@Component
public class UserSessionService {

	@Autowired
	private UserSessionDAO userSessionDAO;

	public List<UserSession> findAll() {
		return userSessionDAO.findAll();
	}

	public UserSession doLogin(User user) {
		UserSession us = new UserSession();
		us.setUser(user);
		us.setLoginTime(new Date());

		String uuid = UUID.randomUUID().toString();
		us.setSession(uuid.replace("-", ""));

		userSessionDAO.saveSession(us);

		return us;
	}
	
	public UserSession doLogin(UserNetwork user) {
		UserSession us = new UserSession();
		us.setUserNetwork(user);
		us.setLoginTime(new Date());

		String uuid = UUID.randomUUID().toString();
		us.setSession(uuid.replace("-", ""));

		userSessionDAO.saveSession(us);

		return us;
	}

	public UserSession findBySession(String session) {
		return userSessionDAO.findBySession(session);
	}
	
	public UserSession findById(int id) {
		return userSessionDAO.findById(id);
	}

	public void deleteSession(String session) {
		userSessionDAO.deleteSession(session);
	}
	
}
