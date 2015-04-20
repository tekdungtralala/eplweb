package com.wiwit.eplweb.model.view;

import java.util.List;

import com.wiwit.eplweb.model.MatchdayComment;

public class MatchdayCommentModelView {

	private List<MatchdayComment> comments;
	private Long totalComment;
	private List<MatchdayComment> myComments;

	public List<MatchdayComment> getComments() {
		return comments;
	}

	public Long getTotalComment() {
		return totalComment;
	}
	
	public List<MatchdayComment> getMyComments() {
		return myComments;
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
}
