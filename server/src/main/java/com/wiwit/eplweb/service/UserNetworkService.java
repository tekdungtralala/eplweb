package com.wiwit.eplweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wiwit.eplweb.dao.UserDAO;
import com.wiwit.eplweb.dao.UserNetworkDAO;
import com.wiwit.eplweb.model.UserNetwork;
import com.wiwit.eplweb.model.input.UserNetworkModelInput;
import com.wiwit.eplweb.util.UserNetworkType;

@Component
public class UserNetworkService {

	@Autowired
	private UserNetworkDAO userNetworkDAO;
	@Autowired
	private UserDAO userDAO;

	public void create(UserNetwork ufn, UserNetworkModelInput model) {
		UserNetwork un = userNetworkDAO.findByEmail(ufn.getEmail());
		
		if (un != null) ufn.setUser(un.getUser());
		
		userNetworkDAO.create(ufn, un == null, model);
	}

	public UserNetwork findByEmailAndType(String email, UserNetworkType type) {
		return userNetworkDAO.findByEmailAndType(email, type);
	}
}
