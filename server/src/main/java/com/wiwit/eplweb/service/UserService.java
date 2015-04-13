package com.wiwit.eplweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wiwit.eplweb.dao.UserDAO;
import com.wiwit.eplweb.model.User;

@Component
public class UserService {

	@Autowired
	private UserDAO userDAO;

	public User findUserByEmail(String email) {
		return userDAO.findByAttribute("email", email);
	}
	
	public User findUserByUsername(String username) {
		return userDAO.findByAttribute("username", username);
	}
}
