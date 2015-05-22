package com.wiwit.eplweb.util;

import java.util.ArrayList;
import java.util.List;

// All available path pattern and that role
public enum PathPattern {
	// Admin Secured path	
	SQUAD_BY_ID(new String[]{"PUT", "DELETE"}, "/api/players/[\\d]+", true, UserRoleHelper.getAdminRole()),
	SQUAD(new String[]{"POST"}, "/api/players", true, UserRoleHelper.getAdminRole()),
	TEAMS_BY_ID(new String[]{"PUT"}, "/api/teams/[\\d]+", true, UserRoleHelper.getAdminRole()),
	
	MATCHDAYS_CHANGE_SCORE(new String[]{"PUT"}, "/api/matchday/[\\d]+/updateScore", true, UserRoleHelper.getAdminRole()),
	
	MATCHDAYS_CHANGE_SCHEDULE(new String[]{"POST"}, "/api/updateMatchday/[\\d]+", true, UserRoleHelper.getAdminRole()),
	
	UPDATE_RANK(new String[]{"POST"}, "/api/updateRanks", true, UserRoleHelper.getAdminRole()),
	
	UPLOAD_FILES(new String[]{"POST"}, "/api/upload/[\\w\\/]+", true, UserRoleHelper.getAdminRole()),	
	DELETE_IMAGE(new String[]{"DELETE"}, "/api/images/[\\d]+", true, UserRoleHelper.getAdminRole()),
	SORTED_IMAGE(new String[]{"PUT"}, "/api/images/sortedImage", true, UserRoleHelper.getAdminRole()),
	
	// User Secured path
	MATCHDAYS_CHANGE_RATING(new String[]{"POST"}, "/api/matchday/[\\d]+/updateRating", true, UserRoleHelper.getUserRole()),
	MATCHDAYS_CHANGE_VOTING(new String[]{"POST"}, "/api/matchday/[\\d]+/updateVoting", true, UserRoleHelper.getUserRole()),
	USER_MY_PROFILE(new String[]{"GET"}, "/api/usernetwork/me", true, UserRoleHelper.getUserRole()),
	POST_MATCHDAY_COMMENTS(new String[]{"POST"}, "/api/matchday/[\\d]+/comment", true, UserRoleHelper.getUserRole()),
	POST_MATCHDAY_COMMENT_POINT(new String[]{"POST"}, "/api/matchday/comment/[\\d]+/point", true, UserRoleHelper.getUserRole()),
	
	// Unsecured path
	STATIC_FILES(new String[]{"GET"}, "^.*\\.(html|css|js|ico|png|jpg|map)$", false),
	BOWER_DIR(new String[]{"GET"}, "/bower_components/.+", false),
	EPLWEB_DIR(new String[]{"GET"}, "/eplweb_components/.+", false),
	
	SLIDE_SHOW(new String[]{"GET"}, "/api/images/[\\w\\/]+", false),
	
	USER_SIGNUP(new String[]{"POST"}, "/api/usernetwork/signin", false),
	USER_SESSION(new String[]{"GET", "DELETE"}, "/api/usernetwork/signin/[\\w]+", false),
	USER_IS_USERNAME_EXIST(new String[]{"POST"}, "/api/user/isUsernameAvailable", false),
	USER_IS_REGISTERED_USER(new String[]{"POST"}, "/api/user/isRegisteredUser", false),
	
	ADMIN_LOGIN(new String[]{"POST"}, "/api/admin/login", false),
	ADMIN_SESSION(new String[]{"GET", "DELETE"}, "/api/admin/login/[\\w]+", false),
	
	CHART_TEAM_STAT(new String[]{"GET"}, "/api/chart/week/[\\d]+/team/[\\d]+", false),
	CHART_FIVE_BIGGEST_TEAM(new String[]{"GET"}, "/api/chart/fiveBigestTeam", false),
	
	INIT_DASHBOARD_PAGE(new String[]{"GET"}, "/api/page/dashboard", false),
	INIT_RANK_PAGE(new String[]{"GET"}, "/api/page/rank", false),
	INIT_MATCHDAY_PAGE(new String[]{"GET"}, "/api/page/matchday", false),
	INIT_TEAM_PAGE(new String[]{"GET"}, "/api/page/team/[\\d]+/[\\w]+", false),
	
	MATCHDAYS(new String[]{"GET"}, "/api/matchday", false),
	MATCHDAYS_BY_WEEK(new String[]{"GET"}, "/api/matchday/[\\d]+", false),
	MATCHDAY_COMMENTS_BY_MATCH(new String[]{"GET"}, "/api/matchday/[\\d]+/comment", false),
	MATCHDAY_COMMENTS_BY_PARENT(new String[]{"GET"}, "/api/matchday/comment/[\\d]+/loadsubcomment", false),
	
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
	private final int role;

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
		this.role = UserRoleHelper.getAnonymousRole();
	}
	
	private PathPattern(String[] methods, String requestPattern,
			boolean securedPath, int role) {
		this.methods = new ArrayList<String>();
		if (methods != new String[]{"GET"} && methods.length > 0) {
			for(String m : methods){
				this.methods.add(m);
			}			
		}
		
		this.requestPattern = requestPattern;
		this.securedPath = securedPath;
		this.role = role;
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
	
	public int getRole() {
		return role;
	}
	
}
