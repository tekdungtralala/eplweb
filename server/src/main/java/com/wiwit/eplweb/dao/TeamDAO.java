package com.wiwit.eplweb.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.wiwit.eplweb.model.Team;

@Service
public class TeamDAO extends AbstractDAO{
	private static final Logger logger = LoggerFactory.getLogger(TeamDAO.class);

	@Transactional
	public List<Team> findAll() {
		openSession();
		List<Team> result = getSession().createQuery("from Team order by Name asc").list();

		logger.info("Team loaded successfully, teams size=" + result.size());
		return result;
	}

	@Transactional
	public Team findByIdAndName(int id, String simpleName) {
		openSession();
		List<Team> list = getSession()
				.createQuery(
						"from Team where id=" + id + " and simpleName='"
								+ simpleName + "'").setMaxResults(1).list();
		if (list == null || list.isEmpty()) {
			logger.info("Can't find Team with team.id=" + id
					+ ", and team.simpleName=" + simpleName);
			return null;
		}
		Team result = list.get(0);

		logger.info("Team loaded successfully, team.id =" + result.getId());
		return result;
	}
	
	@Transactional
	public Team findById(int id) {
		openSession();
		List<Team> list = getSession()
				.createQuery("from Team where id=" + id).setMaxResults(1).list();
		
		if (list == null || list.isEmpty()) {
			logger.info("Can't find Team with team.id=" + id);
			return null;
		}
		Team result = list.get(0);

		logger.info("Team loaded successfully, team.id =" + result.getId());
		return result;
	}
	
	@Transactional
	public void updateTeam(Team team) {
		openSession(true);
		try {
			getSession().update(team);
			commit();
		} catch (Exception e) {
			roleback();
		}
		closeConnection();
	}
}
