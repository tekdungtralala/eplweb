package com.wiwit.eplweb.model.input;

import com.wiwit.eplweb.util.VoteType;

public class VotingModelInput {

	private String vote;

	public String getVote() {
		return vote;
	}

	public void setVote(String vote) {
		this.vote = vote;
	}

	public boolean isValid() {
		if (vote == null || vote.isEmpty())
			return false;
		
		return VoteType.isValidVoteType(vote);
	}
}
