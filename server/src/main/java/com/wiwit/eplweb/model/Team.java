package com.wiwit.eplweb.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
	
	@Column(name = "short_name")
	private String shortName;
	
	@Column(name = "established")
	private String established;
	
	@Column(name = "manager")
	private String manager;
	
	@Column(name = "nickname")
	private String nickname;
	
	@Column(name = "stadium")
	private String stadium;
	
	@Column(name = "longitude")
	private String longitude;
	
	@Column(name = "latitude")
	private String latitude;
	
	@Column(name = "video_id")
	private String videoId;

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
	
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
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
	
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	public String getVideoId() {
		return videoId;
	}
	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	@Override
	public String toString() {
		return "id=" + id + ", name=" + name;
	}
	
	public void copyValue(Team t){
		setName(t.getName());
		setSimpleName(t.getSimpleName());
		setEstablished(t.getEstablished());
		setManager(t.getManager());
		setNickname(t.getManager());
		setLongitude(t.getLongitude());
		setLatitude(t.getLatitude());
		setVideoId(t.getVideoId());
	}

}
