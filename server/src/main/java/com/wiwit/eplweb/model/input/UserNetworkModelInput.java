package com.wiwit.eplweb.model.input;

import com.wiwit.eplweb.util.UserNetworkType;

public class UserNetworkModelInput {

	private String username;
	private String firstName;
	private String lastName;
	private String email;
	private String type;
	private String userNetworkID;
	private String imageUrl;
	
	public String getUsername() {
		return username;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
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
	
	public String getImageUrl() {
		return imageUrl;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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
	
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public boolean isValidModel() {
		if (lastName == null || lastName.isEmpty())
			return false;
		if (firstName == null || firstName.isEmpty())
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
