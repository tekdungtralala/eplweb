package com.wiwit.eplweb.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.wiwit.eplweb.model.Matchday;
import com.wiwit.eplweb.model.MatchdayRating;
import com.wiwit.eplweb.model.MatchdayVoting;

@Service
public class MatchdayDAO extends AbstractDAO{

	private static final Logger logger = LoggerFactory
			.getLogger(MatchdayDAO.class);

	@Transactional
	public List<Matchday> findClosestMatch(int teamId, int weekNumber,
			int totalMatch) {
		openSession();

		String query = "from Matchday as m where m.week.weekNumber > "
				+ (weekNumber - totalMatch) + " and m.week.weekNumber < "
				+ (weekNumber + totalMatch) + " and (homeTeam.id=" + teamId
				+ " or awayTeam.id=" + teamId + ") order by m.week.weekNumber asc";
		List<Matchday> result = getSession().createQuery(query).list();
		logger.info("Matchday loaded successfully, matchdays size="
				+ result.size());
		commitAndClose();
		return result;
	}

	@Transactional
	public List<Matchday> findMatchtdayByWeekNmr(int weekNumber) {
		openSession();

		List<Matchday> result = getSession().createQuery(findMatchdayByWeekNmr(weekNumber)).list();
		logger.info("Matchday loaded successfully, matchdays size="
				+ result.size());
		
		commitAndClose();
		return result;
	}
	
	protected String findMatchdayByWeekNmr(int weekNumber){
		return "from Matchday as m where m.week.weekNumber = " + weekNumber
				+ " order by m.date asc, m.time asc";
	}
	
	@Transactional
	public Matchday findMatchtdayById(int matchdayId) {
		openSession();

		List<Matchday> result = getSession().createQuery(
				"from Matchday as m where m.id = " + matchdayId).list();
		if (result == null || result.size() == 0){
			logger.info("Can't find matchday with id="+ matchdayId);
			return null;
		}
		commitAndClose();
		return result.get(0);
	}
	
	@Transactional
	public void updateMatchday(Matchday matchday) {
		openSession();
		
		getSession().update(matchday);
		
		commitAndClose();
	}
	
	@Transactional
	public void saveMoreMatchday(int weekNumber, List<Matchday> matchdays) {
		openSession();
		Session session = getSession();
		
		// Delete old data
		List<Matchday> result = session.createQuery(findMatchdayByWeekNmr(weekNumber)).list();
		for(Matchday m : result) {
			session.delete(m);
		}
		
		// Create new data
		for(Matchday m : matchdays) {
			session.persist(m);
		}
		commitAndClose();
	}
	
	@Transactional
	public void updateVoting(Matchday matchday, MatchdayVoting mv, boolean newVotingData) {
		openSession();
		Session session = getSession();
		
		if (newVotingData) 
			session.persist(mv);
		else
			session.update(mv);
		
		session.update(matchday);
		
		commitAndClose();
	}
	
	@Transactional
	public void updateRating(Matchday matchday, MatchdayRating mr, boolean newRatingData) {
		openSession();
		Session session = getSession();
		
		if (newRatingData) 
			session.persist(mr); 
		else 
			session.update(mr); 
		
		session.update(matchday);
		
		commitAndClose();
	}
}
