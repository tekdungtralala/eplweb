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

@Entity
@Table(name = "image")
public class Image {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "image_type")
	private String imageType;

	@Column(name = "output_file_name")
	private String outputFileName;

	@Column(name = "local_file_name")
	private String localFileName;

	@Column(name = "file_type")
	private String fileType;
	
	@Column(name = "position")
	private int position;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "team_id", nullable = false)
	private Team team;

	public int getId() {
		return id;
	}

	public String getImageType() {
		return imageType;
	}

	public String getOutputFileName() {
		return outputFileName;
	}

	@JsonIgnore
	public String getLocalFileName() {
		return localFileName;
	}

	public String getFileType() {
		return fileType;
	}
	
	public int getPosition() {
		return position;
	}

	@JsonIgnore
	public Team getTeam() {
		return team;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	public void setOutputFileName(String outputFileName) {
		this.outputFileName = outputFileName;
	}

	public void setLocalFileName(String localFileName) {
		this.localFileName = localFileName;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	
	public void setPosition(int position) {
		this.position = position;
	}

	public void setTeam(Team team) {
		this.team = team;
	}
}
