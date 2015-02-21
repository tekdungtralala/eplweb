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

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

@Entity
@Table(name = "team")
public class Team {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "name")
	private String name;

	@Column(name = "simple_name")
	private String simpleName;

//	@OneToOne(fetch = FetchType.LAZY, mappedBy = "team")
//	@JoinColumn(nullable = false)
//	private Rank rank;
	
	@Column(name = "established")
	private String established;
	
	@Column(name = "manager")
	private String manager;
	
	@Column(name = "nickname")
	private String nickname;
	
	@Column(name = "stadium")
	private String stadium;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getSimpleName() {
		return simpleName;
	}
	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}
	
	public String getEstablished() {
		return established;
	}
	public void setEstablished(String established) {
		this.established = established;
	}
	
	public String getManager() {
		return manager;
	}
	public void setManager(String manager) {
		this.manager = manager;
	}
	
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public String getStadium() {
		return stadium;
	}
	public void setStadium(String stadium) {
		this.stadium = stadium;
	}

//	@JsonIgnore
//	public Rank getRank() {
//		return rank;
//	}
//
//	@JsonProperty
//	public void setRank(Rank rank) {
//		this.rank = rank;
//	}

	@Override
	public String toString() {
		return "id=" + id + ", name=" + name;
	}

}
