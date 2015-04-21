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
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Table(name = "matchday_comment_point")
public class CommentPoint {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "comment_id", nullable = false)
	private MatchdayComment matchdayComment;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "is_up", columnDefinition = "TINYINT", length = 1)
	private Boolean isUp;
	
	@Transient
	private int commentId;

	public int getId() {
		return id;
	}

	@JsonIgnore
	public MatchdayComment getMatchdayComment() {
		return matchdayComment;
	}

	@JsonIgnore
	public User getUser() {
		return user;
	}

	public Boolean getIsUp() {
		return isUp;
	}
	
	public int getCommentId() {
		if (matchdayComment != null && matchdayComment.getId() != 0) {
			return matchdayComment.getId();
		}
		return commentId;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setMatchdayComment(MatchdayComment matchdayComment) {
		this.matchdayComment = matchdayComment;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setIsUp(Boolean isUp) {
		this.isUp = isUp;
	}
	
	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}
}
