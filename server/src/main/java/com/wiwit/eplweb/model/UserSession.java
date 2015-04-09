package com.wiwit.eplweb.model;

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

import org.codehaus.jackson.map.annotate.JsonSerialize;

@Entity
@Table(name = "user_session")
public class UserSession {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = true)
	private User user;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_os_id", nullable = true)
	private UserNetwork userNetwork;

	@Column(name = "role")
	private int role;

	@Column(name = "session")
	private String session;

	@Column(name = "login_time")
	private Date loginTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
	public UserNetwork getUserNetwork() {
		return userNetwork;
	}

	public void setUserNetwork(UserNetwork userNetwork) {
		this.userNetwork = userNetwork;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public String getSession() {
		return session;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}
}
