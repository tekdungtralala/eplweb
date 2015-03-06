package com.wiwit.eplweb.util;

import java.util.ArrayList;
import java.util.List;

public enum PathPattern {
	ADMIN_SESSION(ApiPath.ADMIN_SESSION, null, "/api/admin/login/[\\w]+", false),
	ADMIN_LOGIN(ApiPath.ADMIN_LOGIN, null, "/api/admin/login", false),
	
	CHART_TEAM_STAT(ApiPath.CHART_TEAM_STAT, null, "/api/chart/week/[\\d]+/team/[\\d]+", false),
	CHART_FIVE_BIGGEST_TEAM(ApiPath.CHART_FIVE_BIGGEST_TEAM, null, "/api/chart/fiveBigestTeam", false),
	
	INIT_DASHBOARD_PAGE(ApiPath.INIT_DASHBOARD_PAGE, null, "/api/page/dashboard", false),
	INIT_RANK_PAGE(ApiPath.INIT_RANK_PAGE, null, "/api/page/rank", false),
	INIT_MATCHDAY_PAGE(ApiPath.INIT_MATCHDAY_PAGE, null, "/api/page/matchday", false),
	INIT_TEAM_PAGE(ApiPath.INIT_TEAM_PAGE, null, "/api/page/team/[\\d]+/[\\w]+", false),
	
	MATCHDAYS(ApiPath.MATCHDAYS, null, "/api/matchday", false),
	MATCHDAYS_BY_WEEK(ApiPath.MATCHDAYS_BY_WEEK, null, "/api/matchday/[\\d]+", false),
	
	SQUADS_BY_TEAM(ApiPath.SQUADS_BY_TEAM, null, "/api/players/team/[\\d]+", false),
	
	RANKS(ApiPath.RANKS, null, "/api/ranks", false),
	RANKS_BY_WEEK(ApiPath.RANKS_BY_WEEK, null, "/api/ranks/[\\d]+", false),
	HIGHEST_RANK(ApiPath.HIGHEST_RANK, null, "/api/highestRanks", false),
	
	TEAMS("/api/teams", null, ApiPath.TEAMS, false),
	
	WEEKS("/api/weeks", null, ApiPath.WEEKS, false),
	PASSED_WEEK(ApiPath.PASSED_WEEK, null,"/api/passedWeeks", false),
	
	STATIC_FILES(null, null, "^.*\\.(html|css|js|ico|png|jpg|map)$", false),
	BOWER_DIR(null, null, "/bower_components/.+", false),
	
	// Secured path	
	SQUAD_BY_ID(ApiPath.SQUAD_BY_ID, new String[]{"PUT, DELETE"}, "/api/players/[\\d]+", true),
	SQUAD(ApiPath.SQUAD, new String[]{"POST"}, "/api/players", true);

	private final String requestMapping;
	private final String requestPattern;
	private final List<String> methods;
	private final boolean securedPath;

	private PathPattern(String requestMapping, String[] methods, String requestPattern,
			boolean securedPath) {
		
		this.methods = new ArrayList<String>();
		if (methods != null && methods.length > 0) {
			for(String m : methods){
				this.methods.add(m);
			}			
		}
		
		this.requestMapping = requestMapping;
		this.requestPattern = requestPattern;
		this.securedPath = securedPath;
	}

	public String getRequestMapping() {
		return requestMapping;
	}

	public String getRequestPattern() {
		return requestPattern;
	}

	public boolean isSecuredPath() {
		return securedPath;
	}
	
	public List<String> getMethods() {
		return methods;
	}
	
}
