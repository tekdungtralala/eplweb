package com.wiwit.eplweb.model;

import java.util.Set;

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
	private Set<UserSession> userSessions;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
	private Set<UserNetwork> userNetworks;

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
	public Set<UserSession> getUserSessions() {
		return userSessions;
	}
	
	@JsonIgnore
	public Set<UserNetwork> getUserNetworks() {
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
	public void setUserSessions(Set<UserSession> userSessions) {
		this.userSessions = userSessions;
	}
	
	@JsonIgnore
	public void setUserNetworks(Set<UserNetwork> userNetworks) {
		this.userNetworks = userNetworks;
	}
	
}
