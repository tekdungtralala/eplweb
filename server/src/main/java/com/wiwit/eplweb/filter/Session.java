package com.wiwit.eplweb.filter;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_session")
public class Session {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "session")
	private String session;

	@Column(name = "login_time")
	private Date loginTime;

	@Column(name = "role")
	private int role;

	public int getId() {
		return id;
	}

	public String getSession() {
		return session;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public int getRole() {
		return role;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public void setRole(int role) {
		this.role = role;
	}
}
