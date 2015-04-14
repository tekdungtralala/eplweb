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
@Table(name = "matchday_voting")
public class MatchdayVoting {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "vote")
	private Integer vote;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "matchday_id", nullable = false)
	private Matchday matchday;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	public int getId() {
		return id;
	}

	public Integer getVote() {
		return vote;
	}

	public Matchday getMatchday() {
		return matchday;
	}
	
	public User getUser() {
		return user;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setVote(Integer vote) {
		this.vote = vote;
	}

	public void setMatchday(Matchday matchday) {
		this.matchday = matchday;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
}
