package com.bim.commons.dto;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

public class RequestDTO {

	private String url;
	private MessageProxyDTO message;
	private Map<String, String> headers;

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

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	
	public void addHeader(String key, String value) {
		if(this.headers == null)
			this.headers = new HashMap<String, String>();
		
		this.headers.put(key, value);
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

}
