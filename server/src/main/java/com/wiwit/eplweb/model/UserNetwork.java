package com.wiwit.eplweb.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.wiwit.eplweb.model.input.UserNetworkModelInput;


@Entity
@Table(name = "user_from_network")
public class UserNetwork {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "email")
	private String email;

	@Column(name = "type")
	private String type;

	@Column(name = "user_network_id")
	private String userNetworkID;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
	private List<UserSession> userSessions;
	
	@Transient
	private int userRole;
	
	public UserNetwork() {
	}
	
	public UserNetwork(UserNetworkModelInput model) {
		this.email = model.getEmail();
		this.userNetworkID = model.getUserNetworkID();
		this.type = model.getType().toUpperCase();
	}

	public int getId() {
		return id;
	}
	
	public User getUser() {
		return user;
	}

	public String getEmail() {
		return email;
	}

	public String getType() {
		return type;
	}

	public String getUserNetworkID() {
		return userNetworkID;
	}
	
	@JsonIgnore
	public List<UserSession> getUserSessions() {
		return userSessions;
	}
	
	public int getUserRole() {
		if (userSessions != null && userSessions.size() > 0)
			return userSessions.get(0).getRole();
		return userRole;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@JsonIgnore
	public void setUser(User user) {
		this.user = user;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUserNetworkID(String userNetworkID) {
		this.userNetworkID = userNetworkID;
	}
	
	public void setUserSessions(List<UserSession> userSessions) {
		this.userSessions = userSessions;
	}
	
	public void setUserRole(int userRole) {
		this.userRole = userRole;
	}
}
