package com.wiwit.eplweb.model;

import java.util.Date;
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

@Entity
@Table(name = "matchday_comment")
public class MatchdayComment {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "matchday_id", nullable = false)
	private Matchday matchday;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "value")
	private String value;

	@Column(name = "created")
	private Date created;

	@Column(name = "points", nullable = true)
	private int points;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "parent_id", nullable = true)
	private MatchdayComment parent;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
	private List<MatchdayComment> children;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "matchdayComment")
	private List<CommentPoint> commentPoints;

	@Transient
	private String username;

	@Transient
	private String userImageUrl;

	@Transient
	private int matchdayId;
	
	@Transient
	private List<MatchdayComment> subComment;
	
	@Transient
	private Long totalSubComment;
	
	@Transient
	private int parentId;
	

	public int getId() {
		return id;
	}

	@JsonIgnore
	public Matchday getMatchday() {
		return matchday;
	}

	@JsonIgnore
	public User getUser() {
		return user;
	}

	public String getValue() {
		return value;
	}

	public Date getCreated() {
		return created;
	}

	public int getPoints() {
		return points;
	}

	@JsonIgnore
	public List<MatchdayComment> getChildren() {
		return children;
	}
	
	@JsonIgnore
	public List<CommentPoint> getCommentPoints() {
		return commentPoints;
	}

	@JsonIgnore
	public MatchdayComment getParent() {
		return parent;
	}

	public String getUsername() {
		if (user != null && user.getId() != 0)
			return user.getUsername();
		return username;
	}

	public String getUserImageUrl() {
		if (user != null && user.getId() != 0)
			return user.getImageUrl();
		return userImageUrl;
	}

	public int getMatchdayId() {
		if (matchday != null && matchday.getId() != 0)
			return matchday.getId();
		return matchdayId;
	}
	
	public List<MatchdayComment> getSubComment() {
		return subComment;
	}
	
	public Long getTotalSubComment() {
		return totalSubComment;
	}
	
	public int getParentId() {
		if (parent != null && parent.getId() != 0)
			return parent.getId();
		return parentId;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setMatchday(Matchday matchday) {
		this.matchday = matchday;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public void setParent(MatchdayComment parent) {
		this.parent = parent;
	}

	public void setChildren(List<MatchdayComment> children) {
		this.children = children;
	}
	
	public void setCommentPoints(List<CommentPoint> commentPoints) {
		this.commentPoints = commentPoints;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setUserImageUrl(String userImageUrl) {
		this.userImageUrl = userImageUrl;
	}

	public void setMatchdayId(int matchdayId) {
		this.matchdayId = matchdayId;
	}
	
	public void setSubComment(List<MatchdayComment> subComment) {
		this.subComment = subComment;
	}
	
	public void setTotalSubComment(Long totalSubComment) {
		this.totalSubComment = totalSubComment;
	}
	
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
}
