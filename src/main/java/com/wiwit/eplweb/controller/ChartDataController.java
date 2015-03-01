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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.wiwit.eplweb.model.Phase;
import com.wiwit.eplweb.model.Rank;
import com.wiwit.eplweb.model.view.FiveBigTeamModelView;
import com.wiwit.eplweb.model.view.TeamStatModelView;
import com.wiwit.eplweb.service.PhaseService;
import com.wiwit.eplweb.service.RankService;

@Controller
public class ChartDataController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(ChartDataController.class);

	private PhaseService phaseService;

	private RankService rankService;

	@RequestMapping(value = "/api/chart/week/{weekNumber}/team/{teamId}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public @ResponseBody
	String getChartTeamStat(@PathVariable("weekNumber") int weekNumber,
			@PathVariable("teamId") int teamId) throws JsonGenerationException,
			JsonMappingException, IOException {
		logger.info("GET /api/chart/week/" + weekNumber + "/team/" + teamId);

		List<Rank> ranks = rankService.getRankByWeekNumber(weekNumber);
		TeamStatModelView tsmv = new TeamStatModelView();
		tsmv.addData(teamId, weekNumber, ranks);

		return generateJson(tsmv);
	}

	@RequestMapping(value = "/api/chart/fiveBigestTeam", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	public @ResponseBody
	String getFiveBigestTeam() throws JsonGenerationException,
			JsonMappingException, IOException {
		logger.info("GET /api/chart/fiveBigestTeam");

		Phase p = phaseService.getCurrentMatchday();
		int currWeek = Integer.valueOf(p.getValue());

		FiveBigTeamModelView result = new FiveBigTeamModelView();
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
			result.addData(i, tmp);
		}

		return generateJson(result);
	}

	@Autowired(required = true)
	public void setRankService(RankService rankService) {
		this.rankService = rankService;
	}

	@Autowired(required = true)
	public void setPhaseService(PhaseService phaseService) {
		this.phaseService = phaseService;
	}
}
