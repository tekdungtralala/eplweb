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

@Entity
@Table(name = "user_session")
public class UserSession {
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	
	@Column(name = "session")
	private String session;

	@Column(name = "login_time")
	private Date loginTime;

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
