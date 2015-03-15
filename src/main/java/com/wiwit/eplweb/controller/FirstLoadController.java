package com.wiwit.eplweb.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

	@RequestMapping(value = ApiPath.INIT_TEAM_PAGE, method = RequestMethod.GET, produces = CONTENT_TYPE_JSON)
	public ResponseEntity<TeamPageModelView> getDataTeamPage(@PathVariable("id") int teamId,
			@PathVariable("simpleName") String simpleName)
			throws JsonGenerationException, JsonMappingException, IOException {
		logger.info("GET /api/page/team/" + teamId + "/" + simpleName);

		Team team = teamService.findByIdAndSimppleName(teamId, simpleName);
		if (team == null)
			throw404();

		TeamPageModelView result = new TeamPageModelView();
		result.setTeams(teamService.findAll());
		result.setRanks(rankService.findLatestRank());
		result.setMatchdays(matchdayService.findClosestMatch(teamId));

		return new ResponseEntity<TeamPageModelView>(result, HttpStatus.OK);
	}

	@RequestMapping(value = ApiPath.INIT_MATCHDAY_PAGE, method = RequestMethod.GET, produces = CONTENT_TYPE_JSON)
	public ResponseEntity<MatchdayPageModelView> getDataMatchdayPage()
			throws JsonGenerationException, JsonMappingException, IOException {
		logger.info("GET /api/page/matchday");

		MatchdayPageModelView result = new MatchdayPageModelView();
		result.setWeeks(weekService.getAllWeek());
		result.setMatchdayModelView(matchdayService.findMatchtdayOnCurrWeek());
		return new ResponseEntity<MatchdayPageModelView>(result, HttpStatus.OK);
	}

	@RequestMapping(value = ApiPath.INIT_RANK_PAGE, method = RequestMethod.GET, produces = CONTENT_TYPE_JSON)
	public ResponseEntity<RankPageModelView> getDataRankPage() throws JsonGenerationException,
			JsonMappingException, IOException {
		logger.info("GET /api/page/rank");

		RankPageModelView result = new RankPageModelView();

		result.setRanks(rankService.findLatestRank());

		result.setWeeks(weekService.findAllPassedWeek());

		return new ResponseEntity<RankPageModelView>(result, HttpStatus.OK);
	}

	@RequestMapping(value = ApiPath.INIT_DASHBOARD_PAGE, method = RequestMethod.GET, produces = CONTENT_TYPE_JSON)
	public ResponseEntity<DashboardPageModelView> getDataDashboardPage()
			throws JsonGenerationException, JsonMappingException, IOException {
		logger.info("GET /api/page/dashboard");

		DashboardPageModelView result = new DashboardPageModelView();

		Phase p = phaseService.findCurrentMatchday();
		int currWeek = Integer.valueOf(p.getValue());

		FiveBigTeamModelView fiveBitTeam = new FiveBigTeamModelView();
		List<Rank> bigestRank = rankService.getFiveHighestLastRank();
		for (int i = 1; i < currWeek; i++) {
			// Temporary variable to saving rank team on bigestRank list only
			List<Rank> tmp = new ArrayList<Rank>();

			// Get rank every week from beginning until current week
			List<Rank> rankEveryWeek = rankService.findRankByWeekNumber(i);

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

		result.setMatchday(matchdayService.findMatchtdayOnCurrWeek());

		result.setCurrentWeek(weekService.findCurrWeek());

		return new ResponseEntity<DashboardPageModelView>(result, HttpStatus.OK);
	}
}
