package com.wiwit.eplweb.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wiwit.eplweb.dao.UserSessionDAO;
import com.wiwit.eplweb.model.User;
import com.wiwit.eplweb.model.UserSession;

@Component
public class UserSessionService {

	@Autowired
	private UserSessionDAO userSessionDAO;

	@Transactional
	public List<UserSession> findAll() {
		return userSessionDAO.findAll();
	}

	@Transactional
	public UserSession doLogin(User user) {
		UserSession us = new UserSession();
		us.setUser(user);
		us.setLoginTime(new Date());

		String uuid = UUID.randomUUID().toString();
		us.setSession(uuid.replace("-", ""));

		userSessionDAO.saveSession(us);

		return us;
	}

	@Transactional
	public UserSession findBySession(String session) {
		return userSessionDAO.findBySession(session);
	}

	@Transactional
	public void deleteSession(String session) {
		userSessionDAO.deleteSession(session);
	}
}
