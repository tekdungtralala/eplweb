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

@Entity
@Table(name = "player")
public class Player {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "team_id", nullable = false)
	private Team team;

	@Column(name = "player_number")
	private int playerNumber;

	@Column(name = "name")
	private String name;

	@Column(name = "position")
	private String position;

	public int getId() {
		return id;
	}

	@JsonIgnore
	public Team getTeam() {
		return team;
	}

	public int getPlayerNumber() {
		return playerNumber;
	}

	public String getName() {
		return name;
	}

	public String getPosition() {
		return position;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public void setPlayerNumber(int playerNumber) {
		this.playerNumber = playerNumber;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public void copyValue(Player p) {
		this.name = p.getName();
		this.position = p.getPosition();
	}
}
