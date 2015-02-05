package com.wiwit.eplweb.model;

import java.sql.Time;

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
@Table(name = "machtday")
public class Machtday {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "date")
	private String date;
	
	@Column(name = "time")
	private Time time;
	
	@Column(name = "home_goal")
	private int homeGoal;
	
	@Column(name = "away_goal")
	private int awayGoal;
	
	@Column(name = "home_point")
	private int homePoint;
	
	@Column(name = "away_point")
	private int awayPoint;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "home_team_id", nullable = false)
	private Team homeTeam;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "away_team_id", nullable = false)
	private Team awayTeam;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
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

	public void setHomeGoal(int homeGoal) {
		this.homeGoal = homeGoal;
	}

	public int getAwayGoal() {
		return awayGoal;
	}

	public void setAwayGoal(int awayGoal) {
		this.awayGoal = awayGoal;
	}

	public int getHomePoint() {
		return homePoint;
	}

	public void setHomePoint(int homePoint) {
		this.homePoint = homePoint;
	}

	public int getAwayPoint() {
		return awayPoint;
	}

	public void setAwayPoint(int awayPoint) {
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
}
