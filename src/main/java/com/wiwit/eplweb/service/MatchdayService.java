package com.wiwit.eplweb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wiwit.eplweb.dao.MatchdayDAO;
import com.wiwit.eplweb.model.Matchday;

@Component
@Service
public class MatchdayService {

	@Autowired
	private MatchdayDAO machtdayDAO;
	
	@Transactional
	private List<Matchday> getAllMatchtdayOnLastWeek(){
		return machtdayDAO.getMatchDayOnLastWeek();
	}
}
