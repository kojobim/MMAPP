package com.bim.commons.dto;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class RequestDTO {

	private String url;
	private MessageProxyDTO message;
	private JsonObject body;
	private Map<String, String> headers;
	private Boolean isHttps;

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

	public Boolean isHttps() {
		return isHttps;
	}

	public void setIsHttps(Boolean isHttps) {
		this.isHttps = isHttps;
	}

	public JsonObject getBody() {
		return body;
	}

	public void setBody(JsonObject body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

}
