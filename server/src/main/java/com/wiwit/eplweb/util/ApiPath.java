package com.wiwit.eplweb.util;

public class ApiPath {

	public static final String ADMIN_SESSION = "/api/admin/login/{session}";
	public static final String ADMIN_LOGIN = "/api/admin/login";
	
	public static final String CHART_TEAM_STAT = "/api/chart/week/{weekNumber}/team/{teamId}";
	public static final String CHART_FIVE_BIGGEST_TEAM = "/api/chart/fiveBigestTeam";
	
	public static final String INIT_DASHBOARD_PAGE = "/api/page/dashboard";
	public static final String INIT_RANK_PAGE = "/api/page/rank";
	public static final String INIT_MATCHDAY_PAGE =  "/api/page/matchday";
	public static final String INIT_TEAM_PAGE = "/api/page/team/{id}/{simpleName}";
	
	public static final String MATCHDAYS = "/api/matchday";
	public static final String MATCHDAYS_BY_WEEK = "/api/matchday/{weekNumber}";
	public static final String MATCHDAYS_CHANGE_SCORE = "/api/matchday/{matchdayId}/updateScore";
	public static final String MATCHDAYS_CHANGE_SCHEDULE = "/api/updateMatchday/{weekNumber}";
	
	public static final String SQUAD_BY_ID = "/api/players/{playerId}";
	public static final String SQUAD = "/api/players";
	public static final String SQUADS_BY_TEAM =  "/api/players/team/{teemId}";
	
	public static final String RANKS = "/api/ranks";
	public static final String RANKS_BY_WEEK = "/api/ranks/{weekNumber}";
	public static final String HIGHEST_RANK = "/api/highestRanks";
	public static final String UPDATE_RANK = "/api/updateRanks";
	
	public static final String TEAMS = "/api/teams";
	public static final String TEAMS_BY_ID = "/api/teams/{teamId}";
	
	public static final String WEEKS = "/api/weeks";
	public static final String PASSED_WEEK = "/api/passedWeeks";
	
	public static final String SLIDE_SHOW_UPLOAD = "/api/upload/slideshow/teamId/{teamId}";
	public static final String SLIDE_SHOW = "/api/images/slideshow/teamId/{teamId}";
	
	public static final String IMAGES = "/api/images/{imageId}";
	public static final String IMAGES_SORTED = "/api/images/sortedImage";
	
	public static final String UPLOAD_VIDEO = "/api/upload/video/teamId/{teamId}";
	public static final String CHANGE_VIDEO_THUMBNAIL = "/api/upload/videothumbnail/teamId/{teamId}";
	
	public static final String USER_SESSION = "/api/usernetwork/signin/{session}";
	public static final String USER_SIGNIN = "/api/usernetwork/signin";
}
