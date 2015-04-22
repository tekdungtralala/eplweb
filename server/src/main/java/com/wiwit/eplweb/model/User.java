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
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

@Entity
@Table(name = "user")
public class User {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "username")
	private String username;

	@Column(name = "email")
	private String email;

	@Column(name = "password")
	private String password;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "image_url")
	private String imageUrl;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private List<UserSession> userSessions;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private List<UserNetwork> userNetworks;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private List<MatchdayRating> matchdayRating;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private List<MatchdayVoting> matchdayVoting;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private List<MatchdayComment> matchdayComments;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private List<CommentPoint> commentPoints;
	
	@Transient
	private int userRole;

	public int getId() {
		return id;
	}

	@JsonIgnore
	public String getEmail() {
		return email;
	}

	public String getUsername() {
		return username;
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

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUserSessions(List<UserSession> userSessions) {
		this.userSessions = userSessions;
	}

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

	@JsonIgnore
	public List<MatchdayVoting> getMatchdayVoting() {
		return matchdayVoting;
	}

	public void setMatchdayVoting(List<MatchdayVoting> matchdayVoting) {
		this.matchdayVoting = matchdayVoting;
	}

	@JsonIgnore
	public List<MatchdayComment> getMatchdayComments() {
		return matchdayComments;
	}

	public void setMatchdayComments(List<MatchdayComment> matchdayComments) {
		this.matchdayComments = matchdayComments;
	}

	@JsonIgnore
	public List<CommentPoint> getCommentPoints() {
		return commentPoints;
	}

	public void setCommentPoints(List<CommentPoint> commentPoints) {
		this.commentPoints = commentPoints;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public int getUserRole() {
		if (userSessions != null && userSessions.size() > 0)
			return userSessions.get(0).getRole();
		return userRole;
	}
	
	public void setUserRole(int userRole) {
		this.userRole = userRole;
	}
}
