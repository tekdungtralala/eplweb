package com.wiwit.eplweb.util;

public class RestResult {
	private int code;
	private String message;

	public RestResult(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
