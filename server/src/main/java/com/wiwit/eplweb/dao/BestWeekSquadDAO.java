package com.wiwit.eplweb.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wiwit.eplweb.model.BestWeekSquad;

@Service
public class BestWeekSquadDAO extends AbstractDAO{

	public List<BestWeekSquad> findBestSquadByWeekId(int weekId) {
		openSession();
		List<BestWeekSquad> result =  getSession().createQuery(
				"from BestWeekSquad where week.id = " + weekId + " order by number asc").list();
		return result;
	}
}
