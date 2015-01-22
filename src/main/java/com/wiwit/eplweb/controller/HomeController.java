package com.wiwit.eplweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.wiwit.eplweb.service.RankService;

@Controller
public class HomeController {
	
	private RankService rankService;
	
	@Autowired(required=true)
	@Qualifier(value="rankService")
	public void setRankService(RankService rankService) {
		this.rankService = rankService;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String listPersons(Model model) {
		model.addAttribute("ranks", rankService.getFiveHighestRank());
		return "index";
	}
}
