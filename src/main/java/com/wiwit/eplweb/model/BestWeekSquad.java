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

@Entity
@Table(name = "best_week_squad")
public class BestWeekSquad {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "week_id", nullable = false)
	private Week week;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "player_id", nullable = false)
	private Player player;

	@Column(name = "number")
	private int number;

	public int getId() {
		return id;
	}

	public Week getWeek() {
		return week;
	}

	public Player getPlayer() {
		return player;
	}

	public int getNumber() {
		return number;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setWeek(Week week) {
		this.week = week;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void setNumber(int number) {
		this.number = number;
	}
}
