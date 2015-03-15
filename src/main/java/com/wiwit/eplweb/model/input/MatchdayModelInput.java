package com.wiwit.eplweb.model.input;

import java.sql.Time;
import java.util.Date;

public class MatchdayModelInput {

	private Date date;
	private Time time;
	private int homeTeamId;
	private int awayTeamId;

	public Date getDate() {
		return date;
	}

	public Time getTime() {
		return time;
	}

	public int getHomeTeamId() {
		return homeTeamId;
	}

	public int getAwayTeamId() {
		return awayTeamId;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public void setHomeTeamId(int homeTeamId) {
		this.homeTeamId = homeTeamId;
	}

	public void setAwayTeamId(int awayTeamId) {
		this.awayTeamId = awayTeamId;
	}
}
