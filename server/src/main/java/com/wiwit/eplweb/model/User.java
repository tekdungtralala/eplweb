package com.wiwit.eplweb.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

@Entity
@Table(name = "user")
public class User {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "email")
	private String email;

	@Column(name = "password")
	private String password;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
	private List<UserSession> userSessions;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
	private List<UserNetwork> userNetworks;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private List<MatchdayRating> matchdayRating;

	public int getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}
	
	@JsonIgnore
	public List<UserSession> getUserSessions() {
		return userSessions;
	}
	
	@JsonIgnore
	public List<UserNetwork> getUserNetworks() {
		return userNetworks;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}

	@JsonProperty
	public void setUserSessions(List<UserSession> userSessions) {
		this.userSessions = userSessions;
	}
	
	@JsonIgnore
	public void setUserNetworks(List<UserNetwork> userNetworks) {
		this.userNetworks = userNetworks;
	}
	
	@JsonIgnore
	public List<MatchdayRating> getMatchdayRating() {
		return matchdayRating;
	}
	
	public void setMatchdayRating(List<MatchdayRating> matchdayRating) {
		this.matchdayRating = matchdayRating;
	}
	
}
