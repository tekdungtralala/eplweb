package com.wiwit.eplweb.filter;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
public class SessionService {

	@Autowired
	private SessionDAO sessionDAO;

	@Transactional
	public Session findBySession(String key) {
		return sessionDAO.findBySession(key);
	}

}
