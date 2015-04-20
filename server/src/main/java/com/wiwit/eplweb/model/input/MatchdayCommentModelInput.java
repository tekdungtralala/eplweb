package com.wiwit.eplweb.model.input;

public class MatchdayCommentModelInput {

	private String value;
	private int parentId;

	public String getValue() {
		return value;
	}

	public int getParentId() {
		return parentId;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public boolean isValid() {
		return value != null && !value.isEmpty();
	}

}
