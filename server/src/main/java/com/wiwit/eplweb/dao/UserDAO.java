package com.wiwit.eplweb.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.wiwit.eplweb.model.User;

@Service
public class UserDAO extends AbstractDAO{

	private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);

	@Transactional
	public User findByAttribute(String key, String value) {
		openSession();
		List<User> result = getSession().createQuery(
				"from User as u where u." + key + "= '" + value + "'").list();
		if (result == null || result.size() == 0) {
			return null;
		}
		return result.get(0);
	}
}
