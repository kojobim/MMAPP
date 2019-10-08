package com.bim.msf4j.vo;

import com.google.gson.Gson;

public class PingVO {

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

}