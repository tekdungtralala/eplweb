package com.wiwit.eplweb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wiwit.eplweb.dao.RankDAO;
import com.wiwit.eplweb.model.Rank;

@Component
@Service
public class RankService {

	@Autowired
	private RankDAO rankDAO;

	public void setRankDAO(RankDAO rankDAO) {
		this.rankDAO = rankDAO;
	}

	@Transactional
	public List<Rank> getFiveHighestRank() {
		return this.rankDAO.getFiveHighestRank();
	}
}
