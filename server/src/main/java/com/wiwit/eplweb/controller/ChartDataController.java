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
import com.wiwit.eplweb.model.view.FiveBigTeamModelView;
import com.wiwit.eplweb.model.view.TeamStatModelView;
import com.wiwit.eplweb.service.PhaseService;
import com.wiwit.eplweb.service.RankService;
import com.wiwit.eplweb.util.ApiPath;

@RestController
public class ChartDataController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(ChartDataController.class);

	@Autowired
	private PhaseService phaseService;
	@Autowired
	private RankService rankService;

	// Get the statistic data like how many that team already win, lose and draw.
	// The statistic data covers which team that requested base on "teamId" and 
	//  the data from the average of all teams
	@RequestMapping(value = ApiPath.CHART_TEAM_STAT, method = RequestMethod.GET, produces = CONTENT_TYPE_JSON)
	public ResponseEntity<TeamStatModelView> getChartTeamStat(@PathVariable("weekNumber") int weekNumber,
			@PathVariable("teamId") int teamId) throws JsonGenerationException,
			JsonMappingException, IOException {
		logger.info("GET /api/chart/week/" + weekNumber + "/team/" + teamId);

		List<Rank> ranks = rankService.findRankByWeekNumber(weekNumber);
		TeamStatModelView tsmv = new TeamStatModelView();
		tsmv.addData(teamId, weekNumber, ranks);

		return new ResponseEntity<TeamStatModelView>(tsmv, HttpStatus.OK);
	}

	// Get five biggest team in current week.
	// Used in home page.
	@RequestMapping(value = ApiPath.CHART_FIVE_BIGGEST_TEAM, method = RequestMethod.GET, produces = CONTENT_TYPE_JSON)
	public ResponseEntity<FiveBigTeamModelView> getFiveBiggestTeam() throws JsonGenerationException,
			JsonMappingException, IOException {
		logger.info("GET /api/chart/fiveBigestTeam");

		Phase p = phaseService.findCurrentMatchday();
		int currWeek = Integer.valueOf(p.getValue());

		FiveBigTeamModelView result = new FiveBigTeamModelView();
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
			result.addData(i, tmp);
		}
		return new ResponseEntity<FiveBigTeamModelView>(result, HttpStatus.OK);
	}
}
