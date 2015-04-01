package com.wiwit.eplweb.util;

public class UserRoleHelper {

	public enum UserRoleType {
		ADMIN, USER
	}

	public static int getRole(UserRoleType type) {

		if (type.equals(UserRoleType.ADMIN))
			return 1;
		else if (type.equals(UserRoleType.USER))
			return 2;
		return 0;
	}

	public static int getAdminRole() {
		return getRole(UserRoleType.ADMIN);
	}
	
	public static int getUserRole() {
		return getRole(UserRoleType.USER);
	}
}
