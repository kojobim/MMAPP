package com.bim.msf4j.commons.dto;

public class MessageProxyDTO {

	private String queryParams;
	private String pathParams;
	private String body;

	public String getQueryParams() {
		return queryParams;
	}

	public void setQueryParams(String queryParams) {
		this.queryParams = queryParams;
	}

	public String getPathParams() {
		return pathParams;
	}

	public void setPathParams(String pathParams) {
		this.pathParams = pathParams;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

}