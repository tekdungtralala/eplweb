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
@RequestMapping(value = "/")
public class HomeController {

	private RankService rankService;

	private MatchdayService matchdayService;

	private BestWeekSquadService bestWeekSquadService;
	
	private PhaseService phaseService;
	
	private SeasonService seasonService;

	@RequestMapping(method = RequestMethod.GET)
	public String listPersons(Model model) {

		model.addAttribute("ranks", rankService.getFiveHighestLastRank());

		model.addAttribute("matchday", matchdayService.getMatchtdayOnCurrWeek()
				.getModel());

		model.addAttribute("squad", bestWeekSquadService.getBestSquadLastWeek());
		
		// TODO if this is first match day hide time of the week on index
		int previousWeek = Integer.valueOf(phaseService.getCurrentMatchday().getValue()) - 1;
		model.addAttribute("previousWeek", previousWeek);
		
		String seasonId = phaseService.getCurrentSeason().getValue();
		model.addAttribute("currentSeason", seasonService.getSeasonById(seasonId).getYears());
		
		// TODO uniform on index.html still default
		return "index";
	}
	
	@Autowired(required = true)
	public void setSeasonService(SeasonService seasonService) {
		this.seasonService = seasonService;
	}
	
	@Autowired(required = true)
	public void setPhaseService(PhaseService phaseService) {
		this.phaseService = phaseService;
	}

	@Autowired(required = true)
	public void setBestWeekSquadService(
			BestWeekSquadService bestWeekSquadService) {
		this.bestWeekSquadService = bestWeekSquadService;
	}

	@Autowired(required = true)
	public void setRankService(RankService rankService) {
		this.rankService = rankService;
	}

	@Autowired(required = true)
	public void setMatchdayService(MatchdayService matchdayService) {
		this.matchdayService = matchdayService;
	}
}
