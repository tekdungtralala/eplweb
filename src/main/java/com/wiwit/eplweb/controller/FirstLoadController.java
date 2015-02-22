package com.wiwit.eplweb.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wiwit.eplweb.model.Phase;
import com.wiwit.eplweb.model.Rank;
import com.wiwit.eplweb.model.Team;
import com.wiwit.eplweb.model.view.DashboardPageModelView;
import com.wiwit.eplweb.model.view.FiveBigTeamModelView;
import com.wiwit.eplweb.model.view.MatchdayPageModelView;
import com.wiwit.eplweb.model.view.RankPageModelView;
import com.wiwit.eplweb.model.view.TeamPageModelView;
import com.wiwit.eplweb.service.MatchdayService;
import com.wiwit.eplweb.service.PhaseService;
import com.wiwit.eplweb.service.RankService;
import com.wiwit.eplweb.service.TeamService;
import com.wiwit.eplweb.service.WeekService;

@Controller
public class FirstLoadController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(FirstLoadController.class);

	private PhaseService phaseService;
	private RankService rankService;
	private MatchdayService matchdayService;
	private WeekService weekService;
	private TeamService teamService;

	@RequestMapping(value = "/api/page/team/{id}/{simpleName}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public @ResponseBody
	String getDataTeamPage(@PathVariable("id") int teamId,
			@PathVariable("simpleName") String simpleName)
			throws JsonGenerationException, JsonMappingException, IOException {
		logger.info("GET /api/page/team/" + teamId + "/" + simpleName);
		
		Team team = teamService.findByIdAndSimppleName(teamId, simpleName);
		if (team == null) throw404();
		
		TeamPageModelView result = new TeamPageModelView();
		result.setTeams(teamService.findAll());
		result.setRanks(rankService.getLatestRank());
		result.setMatchdays(matchdayService.getLastAndNextMatchday(teamId));

		return generateJson(result);
	}

	@RequestMapping(value = "/api/page/matchday", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public @ResponseBody
	String getDataMatchdayPage(Model model) throws JsonGenerationException,
			JsonMappingException, IOException {
		logger.info("GET /api/page/matchday");

		MatchdayPageModelView result = new MatchdayPageModelView();
		result.setWeeks(weekService.getAllWeek());
		result.setMatchdayModelView(matchdayService.getMatchtdayOnCurrWeek());
		return generateJson(result);
	}

	@RequestMapping(value = "/api/page/rank", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public @ResponseBody
	String getDataRankPage(Model model) throws JsonGenerationException,
			JsonMappingException, IOException {
		logger.info("GET /api/page/rank");

		RankPageModelView result = new RankPageModelView();

		result.setRanks(rankService.getLatestRank());

		result.setWeeks(weekService.getAllPassedWeek());

		return generateJson(result);
	}

	@RequestMapping(value = "/api/page/dashboard", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public @ResponseBody
	String getDataDashboardPage(Model model) throws JsonGenerationException,
			JsonMappingException, IOException {
		logger.info("GET /api/page/dashboard");

		DashboardPageModelView result = new DashboardPageModelView();

		Phase p = phaseService.getCurrentMatchday();
		int currWeek = Integer.valueOf(p.getValue());

		FiveBigTeamModelView fiveBitTeam = new FiveBigTeamModelView();
		List<Rank> bigestRank = rankService.getFiveHighestLastRank();
		for (int i = 1; i < currWeek; i++) {
			// Temporary variable to saving rank team on bigestRank list only
			List<Rank> tmp = new ArrayList<Rank>();

			// Get rank every week from beginning until current week
			List<Rank> rankEveryWeek = rankService.getRankByWeekNumber(i);

			// From rankEveryWeek only get selected team and put them on tmp
			for (Rank br : bigestRank) {
				for (Rank r : rankEveryWeek) {
					if (br.getTeam().getId() == r.getTeam().getId()) {
						tmp.add(r);
					}
				}
			}

			// Now, tmp var only has team on bigestRank list,
			// then put them to model view
			fiveBitTeam.addData(i, tmp);
		}
		result.setFiveBigTeam(fiveBitTeam);

		result.setHighestRank(rankService.getFiveHighestLastRank());

		result.setMatchday(matchdayService.getMatchtdayOnCurrWeek());

		result.setCurrentWeek(weekService.findCurrWeek());

		return generateJson(result);
	}
	
	@Autowired(required = true)
	public void setTeamService(TeamService teamService) {
		this.teamService = teamService;
	}

	@Autowired(required = true)
	public void setWeekService(WeekService weekService) {
		this.weekService = weekService;
	}

	@Autowired(required = true)
	public void setMatchdayService(MatchdayService matchdayService) {
		this.matchdayService = matchdayService;
	}

	@Autowired(required = true)
	public void setPhaseService(PhaseService phaseService) {
		this.phaseService = phaseService;
	}

	@Autowired(required = true)
	public void setRankService(RankService rankService) {
		this.rankService = rankService;
	}
}
