package com.bim.msf4j.commons.dto;

import com.google.gson.Gson;

public class RequestDTO {

	private String url;
	private MessageProxyDTO message;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public MessageProxyDTO getMessage() {
		return message;
	}

	public void setMessage(MessageProxyDTO message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

}