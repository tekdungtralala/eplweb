package com.wiwit.eplweb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wiwit.eplweb.dao.MachtdayDAO;
import com.wiwit.eplweb.model.Machtday;

@Component
@Service
public class MachtdayService {

	@Autowired
	private MachtdayDAO machtdayDAO;
	
	@Transactional
	private List<Machtday> getAllMatchtdayOnLastWeek(){
		return machtdayDAO.getMatchDayOnLastWeek();
	}
}
