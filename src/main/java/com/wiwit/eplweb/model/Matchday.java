package com.wiwit.eplweb.model;

import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "matchday")
public class Matchday {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "date")
	private Date date;
	
	@Column(name = "time")
	private Time time;
	
	@Column(name = "home_goal")
	private Integer homeGoal;
	
	@Column(name = "away_goal")
	private Integer awayGoal;
	
	@Column(name = "home_point")
	private Integer homePoint;
	
	@Column(name = "away_point")
	private Integer awayPoint;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "home_team_id", nullable = false)
	private Team homeTeam;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "away_team_id", nullable = false)
	private Team awayTeam;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "week_id", nullable = false)
	private Week week;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public int getHomeGoal() {
		return homeGoal;
	}

	public void setHomeGoal(Integer homeGoal) {
		this.homeGoal = homeGoal;
	}

	public int getAwayGoal() {
		return awayGoal;
	}

	public void setAwayGoal(Integer awayGoal) {
		this.awayGoal = awayGoal;
	}

	public int getHomePoint() {
		return homePoint;
	}

	public void setHomePoint(Integer homePoint) {
		this.homePoint = homePoint;
	}

	public int getAwayPoint() {
		return awayPoint;
	}

	public void setAwayPoint(Integer awayPoint) {
		this.awayPoint = awayPoint;
	}

	public Team getHomeTeam() {
		return homeTeam;
	}

	public void setHomeTeam(Team homeTeam) {
		this.homeTeam = homeTeam;
	}

	public Team getAwayTeam() {
		return awayTeam;
	}

	public void setAwayTeam(Team awayTeam) {
		this.awayTeam = awayTeam;
	}

	public Week getWeek() {
		return week;
	}

	public void setWeek(Week week) {
		this.week = week;
	}
}
