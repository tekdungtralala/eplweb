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
	private String id;
	
	@Column(name = "session")
	private String session;

	@Column(name = "login_time")
	private Date loginTime;
	
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
