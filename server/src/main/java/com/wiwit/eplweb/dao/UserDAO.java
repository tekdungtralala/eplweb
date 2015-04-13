package com.wiwit.eplweb.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wiwit.eplweb.model.User;

@Service
public class UserDAO {

	private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Transactional
	public User findByAttribute(String key, String value) {
		Session session = this.sessionFactory.openSession();
		List<User> result = session.createQuery(
				"from User as u where u." + key + "= '" + value + "'").list();
		session.close();
		if (result == null || result.size() == 0) {
			return null;
		}
		return result.get(0);
	}
}
