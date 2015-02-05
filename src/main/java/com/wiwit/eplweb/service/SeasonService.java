package com.wiwit.eplweb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wiwit.eplweb.dao.SeasonDAO;
import com.wiwit.eplweb.model.Season;

@Component
@Service
public class SeasonService {

	@Autowired
	private SeasonDAO seasonDAO;
	
	@Transactional
	public List<Season> getAllSeason(){
		return seasonDAO.getAllSeason();
	}
}
