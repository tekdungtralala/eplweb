package com.wiwit.eplweb.filter;

import java.util.List;

import javax.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessionDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Transactional
	public com.wiwit.eplweb.filter.Session findBySession(String session) {
		Session se = this.sessionFactory.openSession();
		List<com.wiwit.eplweb.filter.Session> list = se.createQuery(
				"from Session where session='" + session + "'").list();

		se.close();
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

}
