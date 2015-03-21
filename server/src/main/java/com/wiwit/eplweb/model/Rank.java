package com.wiwit.eplweb.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

@Entity
@Table(name = "rank")
public class Rank {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "games_won")
	private int gamesWon;

	@Column(name = "games_drawn")
	private int gamesDrawn;

	@Column(name = "games_lost")
	private int gamesLost;

	@Column(name = "goals_scored")
	private int goalsScored;

	@Column(name = "goals_against")
	private int goalsAgainst;

	@Column(name = "points")
	private int points;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "team_id", nullable = false)
	private Team team;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "week_id", nullable = false)
	private Week week;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getGamesWon() {
		return gamesWon;
	}

	public void setGamesWon(int gamesWon) {
		this.gamesWon = gamesWon;
	}

	public int getGamesDrawn() {
		return gamesDrawn;
	}

	public void setGamesDrawn(int gamesDrawn) {
		this.gamesDrawn = gamesDrawn;
	}

	public int getGamesLost() {
		return gamesLost;
	}

	public void setGamesLost(int gamesLost) {
		this.gamesLost = gamesLost;
	}

	public int getGoalsScored() {
		return goalsScored;
	}

	public void setGoalsScored(int goalsScored) {
		this.goalsScored = goalsScored;
	}

	public int getGoalsAgainst() {
		return goalsAgainst;
	}

	public void setGoalsAgainst(int goalsAgainst) {
		this.goalsAgainst = goalsAgainst;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}
	
	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public String toString() {
		return "id= " + id + ", points=" + points + ", team=" + team.getName();
	}

	@JsonIgnore
	public Week getWeek() {
		return week;
	}

	@JsonProperty
	public void setWeek(Week week) {
		this.week = week;
	}
}
