package com.wiwit.eplweb.model.view;

import java.util.List;

import com.wiwit.eplweb.model.MatchdayComment;

public class MatchdayCommentModelView {

	private List<MatchdayComment> comments;
	private Long totalComment;

	public List<MatchdayComment> getComments() {
		return comments;
	}

	public Long getTotalComment() {
		return totalComment;
	}

	public void setComments(List<MatchdayComment> comments) {
		this.comments = comments;
	}

	public void setTotalComment(Long totalComment) {
		this.totalComment = totalComment;
	}
}
