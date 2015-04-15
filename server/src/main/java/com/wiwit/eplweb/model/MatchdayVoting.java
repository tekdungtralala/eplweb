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
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

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
	
	@Transient
	private int matchdayId;
	@Transient
	private int userId;

	public int getId() {
		return id;
	}

	public Integer getVote() {
		return vote;
	}

	@JsonIgnore
	public Matchday getMatchday() {
		return matchday;
	}
	
	@JsonIgnore
	public User getUser() {
		return user;
	}
	
	public int getMatchdayId() {
		if (matchday != null && matchday.getId() != 0) 
			return matchday.getId();
		return matchdayId;
	}
	
	public int getUserId() {
		if (user != null && user.getId() != 0) 
			return user.getId();
		return userId;
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
	
	public void setMatchdayId(int matchdayId) {
		this.matchdayId = matchdayId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
}
