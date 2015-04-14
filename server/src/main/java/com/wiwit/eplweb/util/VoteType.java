package com.wiwit.eplweb.util;

public enum VoteType {

	HOME(1), AWAY(2), TIE(3);

	private int value;

	private VoteType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public static VoteType getVote(int value) {
		if (value == VoteType.HOME.getValue()) {
			return VoteType.HOME;
		}
		if (value == VoteType.AWAY.getValue()) {
			return VoteType.AWAY;
		}
		if (value == VoteType.TIE.getValue()) {
			return VoteType.TIE;
		}
		return null;
	}

	public static VoteType getVote(String value) {
		if (value.equalsIgnoreCase(VoteType.HOME.toString())) {
			return VoteType.HOME;
		}
		if (value.equalsIgnoreCase(VoteType.AWAY.toString())) {
			return VoteType.AWAY;
		}
		if (value.equalsIgnoreCase(VoteType.TIE.toString())) {
			return VoteType.TIE;
		}
		return null;
	}

	public static boolean isValidVoteType(String vote) {
		if (vote.equalsIgnoreCase((VoteType.HOME.toString()))) {
			return true;
		}
		if (vote.equalsIgnoreCase((VoteType.AWAY.toString()))) {
			return true;
		}
		if (vote.equalsIgnoreCase((VoteType.TIE.toString()))) {
			return true;
		}
		return false;
	}
}
