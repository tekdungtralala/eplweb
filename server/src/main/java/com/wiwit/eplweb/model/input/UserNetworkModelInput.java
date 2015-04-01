package com.wiwit.eplweb.model.input;

import com.wiwit.eplweb.util.UserNetworkType;

public class UserNetworkModelInput {

	private String name;
	private String email;
	private String type;
	private String userNetworkID;

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getType() {
		return type;
	}

	public String getUserNetworkID() {
		return userNetworkID;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUserNetworkID(String userNetworkID) {
		this.userNetworkID = userNetworkID;
	}

	public boolean isValidModel() {
		if (name == null || name.isEmpty())
			return false;
		if (email == null || email.isEmpty())
			return false;
		if (userNetworkID == null || userNetworkID.isEmpty())
			return false;
		if (type == null || type.isEmpty())
			return false;

		try {
			UserNetworkType.valueOf(type.toUpperCase());
		} catch (Exception e) {
			return false;
		}

		return true;
	}
}
