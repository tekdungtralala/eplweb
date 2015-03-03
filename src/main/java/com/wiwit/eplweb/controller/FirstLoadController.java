package com.wiwit.eplweb.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
import com.wiwit.eplweb.util.ApiPath;

@RestController
public class FirstLoadController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(FirstLoadController.class);

	@Autowired
	private PhaseService phaseService;
	@Autowired
	private RankService rankService;
	@Autowired
	private MatchdayService matchdayService;
	@Autowired
	private WeekService weekService;
	@Autowired
	private TeamService teamService;

	@RequestMapping(value = ApiPath.INIT_TEAM_PAGE, method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public TeamPageModelView getDataTeamPage(@PathVariable("id") int teamId,
			@PathVariable("simpleName") String simpleName)
			throws JsonGenerationException, JsonMappingException, IOException {
		logger.info("GET /api/page/team/" + teamId + "/" + simpleName);

		Team team = teamService.findByIdAndSimppleName(teamId, simpleName);
		if (team == null)
			throw404();

		TeamPageModelView result = new TeamPageModelView();
		result.setTeams(teamService.findAll());
		result.setRanks(rankService.getLatestRank());
		result.setMatchdays(matchdayService.getLastAndNextMatchday(teamId));

		return result;
	}

	@RequestMapping(value = ApiPath.INIT_MATCHDAY_PAGE, method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public MatchdayPageModelView getDataMatchdayPage()
			throws JsonGenerationException, JsonMappingException, IOException {
		logger.info("GET /api/page/matchday");

		MatchdayPageModelView result = new MatchdayPageModelView();
		result.setWeeks(weekService.getAllWeek());
		result.setMatchdayModelView(matchdayService.getMatchtdayOnCurrWeek());
		return result;
	}

	@RequestMapping(value = ApiPath.INIT_RANK_PAGE, method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public RankPageModelView getDataRankPage() throws JsonGenerationException,
			JsonMappingException, IOException {
		logger.info("GET /api/page/rank");

		RankPageModelView result = new RankPageModelView();

		result.setRanks(rankService.getLatestRank());

		result.setWeeks(weekService.getAllPassedWeek());

		return result;
	}

	@RequestMapping(value = ApiPath.INIT_DASHBOARD_PAGE, method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public DashboardPageModelView getDataDashboardPage()
			throws JsonGenerationException, JsonMappingException, IOException {
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

		return result;
	}
}
