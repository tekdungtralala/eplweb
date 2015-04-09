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

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

@Entity
@Table(name = "season")
public class Season {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "years")
	private String years;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "season")
	private List<Week> weeks;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getYears() {
		return years;
	}

	public void setYears(String years) {
		this.years = years;
	}

	@JsonIgnore
	public List<Week> getWeeks() {
		return weeks;
	}

	@JsonProperty
	public void setWeek(List<Week> weeks) {
		this.weeks = weeks;
	}
}
