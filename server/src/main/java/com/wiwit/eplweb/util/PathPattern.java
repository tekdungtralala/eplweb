package com.wiwit.eplweb.util;

import java.util.ArrayList;
import java.util.List;

public enum PathPattern {
	// Secured path	
	SQUAD_BY_ID(new String[]{"PUT", "DELETE"}, "/api/players/[\\d]+", true),
	SQUAD(new String[]{"POST"}, "/api/players", true),
	TEAMS_BY_ID(new String[]{"PUT"}, "/api/teams/[\\d]+", true),
	MATCHDAYS_CHANGE_SCORE(new String[]{"PUT"}, 
			"/api/matchday/[\\d]+/updateScore", true),
	MATCHDAYS_CHANGE_SCHEDULE(new String[]{"POST"}, 
			"/api/updateMatchday/[\\d]+", true),
	UPDATE_RANK(new String[]{"POST"}, "/api/updateRanks", true),
	
	UPLOAD_FILES(new String[]{"POST"}, "/api/upload/[\\w\\/]+", true),	
	DELETE_IMAGE(new String[]{"DELETE"}, "/api/images/[\\d]+", true),
	SORTED_IMAGE(new String[]{"PUT"}, "/api/images/sortedImage", true),
	
	// Unsecured path
	STATIC_FILES(new String[]{"GET"}, "^.*\\.(html|css|js|ico|png|jpg|map)$", false),
	BOWER_DIR(new String[]{"GET"}, "/bower_components/.+", false),
	
	SLIDE_SHOW(new String[]{"GET"}, "/api/images/[\\w\\/]+", false),
	
	ADMIN_LOGIN(new String[]{"POST"}, "/api/admin/login", false),
	ADMIN_SESSION(new String[]{"GET"}, "/api/admin/login/[\\w]+", false),
	
	CHART_TEAM_STAT(new String[]{"GET"}, "/api/chart/week/[\\d]+/team/[\\d]+", false),
	CHART_FIVE_BIGGEST_TEAM(new String[]{"GET"}, "/api/chart/fiveBigestTeam", false),
	
	INIT_DASHBOARD_PAGE(new String[]{"GET"}, "/api/page/dashboard", false),
	INIT_RANK_PAGE(new String[]{"GET"}, "/api/page/rank", false),
	INIT_MATCHDAY_PAGE(new String[]{"GET"}, "/api/page/matchday", false),
	INIT_TEAM_PAGE(new String[]{"GET"}, "/api/page/team/[\\d]+/[\\w]+", false),
	
	MATCHDAYS(new String[]{"GET"}, "/api/matchday", false),
	MATCHDAYS_BY_WEEK(new String[]{"GET"}, "/api/matchday/[\\d]+", false),
	
	SQUADS_BY_TEAM(new String[]{"GET"}, "/api/players/team/[\\d]+", false),
	
	RANKS(new String[]{"GET"}, "/api/ranks", false),
	RANKS_BY_WEEK(new String[]{"GET"}, "/api/ranks/[\\d]+", false),
	HIGHEST_RANK(new String[]{"GET"}, "/api/highestRanks", false),
	
	TEAMS(new String[]{"GET"}, "/api/teams", false),
	WEEKS(new String[]{"GET"}, "/api/weeks", false),
	
	PASSED_WEEK(new String[]{"GET"},"/api/passedWeeks", false)
	;

	private final String requestPattern;
	private final List<String> methods;
	private final boolean securedPath;

	private PathPattern(String[] methods, String requestPattern,
			boolean securedPath) {
		
		this.methods = new ArrayList<String>();
		if (methods != new String[]{"GET"} && methods.length > 0) {
			for(String m : methods){
				this.methods.add(m);
			}			
		}
		
		this.requestPattern = requestPattern;
		this.securedPath = securedPath;
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
