package com.wiwit.eplweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.wiwit.eplweb.service.MatchdayService;
import com.wiwit.eplweb.service.RankService;

@Controller
@RequestMapping(value = "/")
public class HomeController {
	
	private RankService rankService;
	
	private MatchdayService matchdayService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String listPersons(Model model) {
		
		model.addAttribute("ranks", rankService.getFiveHighestLastRank());
		
		model.addAttribute("matchday", matchdayService.getMatchtdayOnCurrWeek().getModel());
		
		return "index";
	}
	
	@Autowired(required=true)
	public void setRankService(RankService rankService) {
		this.rankService = rankService;
	}
	
	@Autowired(required=true)
	public void setMatchdayService(MatchdayService matchdayService) {
		this.matchdayService = matchdayService;
	}
}
