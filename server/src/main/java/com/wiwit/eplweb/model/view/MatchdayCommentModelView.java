package com.wiwit.eplweb.model.view;

import java.util.List;

import com.wiwit.eplweb.model.CommentPoint;
import com.wiwit.eplweb.model.MatchdayComment;

public class MatchdayCommentModelView {

	private List<MatchdayComment> comments;
	private Long totalComment;
	private List<MatchdayComment> myComments;
	private List<CommentPoint> myPoints;

	public List<MatchdayComment> getComments() {
		return comments;
	}

	public Long getTotalComment() {
		return totalComment;
	}

	public List<MatchdayComment> getMyComments() {
		return myComments;
	}

	public List<CommentPoint> getMyPoints() {
		return myPoints;
	}

	public void setComments(List<MatchdayComment> comments) {
		this.comments = comments;
	}

	public void setTotalComment(Long totalComment) {
		this.totalComment = totalComment;
	}

	public void setMyComments(List<MatchdayComment> myComments) {
		this.myComments = myComments;
	}

	public void setMyPoints(List<CommentPoint> myPoints) {
		this.myPoints = myPoints;
	}
}
