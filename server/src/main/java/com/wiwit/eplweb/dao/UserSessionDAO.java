package com.wiwit.eplweb.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

import com.wiwit.eplweb.model.UserSession;
import com.wiwit.eplweb.util.UserRoleHelper;

@Service
public class UserSessionDAO extends AbstractDAO{

	@Transactional
	public List<UserSession> findAll() {
		openSession();
		List<UserSession> result =  getSession().createQuery("from UserSession").list();
		return result;
	}
	
	@Transactional
	public UserSession findById(int id) {
		openSession();
		List<UserSession> list = getSession().createQuery(
				"from UserSession where id=" + id + "").list();
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

	@Transactional
	public UserSession findBySession(String session) {
		openSession();
		List<UserSession> list = getSession().createQuery(
				"from UserSession where session='" + session + "'").list();
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

	@Transactional
	public void saveSession(UserSession us) {
		openSession(true);
		try {
			Session se = getSession();

			// Delete previous session
			if (us.getUser() != null){
				Query q = se.createQuery("DELETE UserSession where user.id = "
						+ us.getUser().getId() + "");
				q.executeUpdate();
				us.setRole(UserRoleHelper.getAdminRole());
			} else if (us.getUserNetwork() != null) {
				Query q = se.createQuery("DELETE UserSession where userNetwork.id = "
						+ us.getUserNetwork().getId() + "");
				q.executeUpdate();
				us.setRole(UserRoleHelper.getUserRole());
			}

			// Save new session
			se.persist(us);
			
			commit();
		} catch (Exception e) {
			roleback();
		}
		closeConnection();
	}

	@Transactional
	public void deleteSession(String session) {
		openSession(true);
		try {
			Query q = getSession().createQuery("DELETE UserSession where session = '"
					+ session + "'");
			q.executeUpdate();
			commit();
		} catch (Exception e) {
			roleback();
		}
		closeConnection();
	}

}
