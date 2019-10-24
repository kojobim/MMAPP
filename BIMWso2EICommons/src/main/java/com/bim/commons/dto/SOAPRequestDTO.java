package com.bim.commons.dto;

import javax.xml.soap.SOAPElement;

import com.google.gson.Gson;

public class SOAPRequestDTO {
	
	private String soapEnpoint;
	private String soapAction;
	private String namespace;
	private String namespaceUrl;
	private SOAPElement soapElementBody;
	private String soapRequestPrefix;


	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getNamespaceUrl() {
		return namespaceUrl;
	}

	public void setNamespaceUrl(String namespaceUrl) {
		this.namespaceUrl = namespaceUrl;
	}

	public String getSoapEnpoint() {
		return soapEnpoint;
	}

	public void setSoapEnpoint(String soapEnpoint) {
		this.soapEnpoint = soapEnpoint;
	}

	public String getSoapAction() {
		return soapAction;
	}

	public void setSoapAction(String soapAction) {
		this.soapAction = soapAction;
	}

	public SOAPElement getSoapElementBody() {
		return soapElementBody;
	}

	public void setSoapElementBody(SOAPElement soapElementBody) {
		this.soapElementBody = soapElementBody;
	}

	public String getSoapRequestPrefix() {
		return soapRequestPrefix;
	}

	public void setSoapRequestPrefix(String soapRequestPrefix) {
		this.soapRequestPrefix = soapRequestPrefix;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

}