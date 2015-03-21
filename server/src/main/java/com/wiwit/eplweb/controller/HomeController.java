package com.wiwit.eplweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.wiwit.eplweb.service.BestWeekSquadService;
import com.wiwit.eplweb.service.MatchdayService;
import com.wiwit.eplweb.service.PhaseService;
import com.wiwit.eplweb.service.RankService;
import com.wiwit.eplweb.service.SeasonService;

@Controller
public class HomeController {
	
	@Autowired
	private RankService rankService;
	@Autowired
	private MatchdayService matchdayService;
	@Autowired
	private BestWeekSquadService bestWeekSquadService;
	@Autowired
	private PhaseService phaseService;
	@Autowired
	private SeasonService seasonService;

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String listPersons(Model model) {

		model.addAttribute("ranks", rankService.getFiveHighestLastRank());

		model.addAttribute("matchday", matchdayService.findMatchtdayOnCurrWeek()
				.getModel());

		model.addAttribute("squad", bestWeekSquadService.findBestSquadLastWeek());
		
		// TODO if this is first match day hide time of the week on index
		int previousWeek = Integer.valueOf(phaseService.findCurrentMatchday().getValue()) - 1;
		model.addAttribute("previousWeek", previousWeek);
		
		String seasonId = phaseService.findCurrentSeason().getValue();
		model.addAttribute("currentSeason", seasonService.findSeasonById(seasonId).getYears());
		
		// TODO uniform on index.html still default
		return "index";
	}
}
