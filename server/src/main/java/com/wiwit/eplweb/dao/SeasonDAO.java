package com.wiwit.eplweb.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wiwit.eplweb.model.Season;

@Service
public class SeasonDAO extends AbstractDAO{

	public List<Season> findAllSeason() {
		openSession();
		List<Season> result = getSession().createQuery("from Season").list();
		return result;
	}

	public Season findSeasonById(Object id) {
		openSession();
		Season result = (Season) getSession().createQuery("from Season where id = " + id)
				.list().get(0);
				
		return result;
	}
}
