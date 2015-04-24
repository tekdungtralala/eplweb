package com.wiwit.eplweb.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.wiwit.eplweb.model.User;
import com.wiwit.eplweb.model.UserNetwork;
import com.wiwit.eplweb.model.input.UserNetworkModelInput;
import com.wiwit.eplweb.util.UserNetworkType;

@Service
public class UserNetworkDAO extends AbstractDAO{

	private static final Logger logger = LoggerFactory.getLogger(UserNetworkDAO.class);
	
	@Transactional
	public void create(UserNetwork userNetwork, boolean newUser, 
			UserNetworkModelInput model) {
		openSession(true);
		try {
			if (newUser) {
				User user = new User();
				user.setUsername(model.getUsername());
				user.setFirstName(model.getFirstName());
				user.setLastName(model.getLastName());
				user.setImageUrl(model.getImageUrl());
				getSession().persist(user);
				
				userNetwork.setUser(user);
			}
			getSession().persist(userNetwork);			
			commit();
		} catch (Exception e) {
			roleback();
		}
		closeConnection();
	}
	
	@Transactional
	public void create(UserNetwork un, User usr) {
		openSession(true);
		try {
			getSession().persist(un);
			getSession().persist(usr);
			
			commit();
		} catch (Exception e) {
			roleback();
		}
		closeConnection();
	}
	
	@Transactional
	public UserNetwork findByEmailAndType(String email, UserNetworkType type) {
		openSession();
		List<UserNetwork> results = getSession().createQuery("from UserNetwork where " +
				"email='" + email + "' and type='" + type.getValue() + "'").list();
		if (results != null && results.size() > 0)
			return results.get(0);
		return null;
	}
	
	@Transactional
	public UserNetwork findByEmail(String email) {
		openSession();
		List<UserNetwork> results = getSession().createQuery("from UserNetwork where " +
				"email='" + email + "'").list();
		if (results != null && results.size() > 0)
			return results.get(0);
		return null;
	}
}
