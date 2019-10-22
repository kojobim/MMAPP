package com.bim.commons.dto;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class SOAPRequestDTO {
	
	private String soapEnpoint;
	private String soapAction;
	private String namespace;
	private String namespaceUrl;
	private String parentName;
	private String childNamespace;
	private String childNamesapacePrefix;
	private JsonElement data;

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

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public JsonElement getData() {
		return data;
	}

	public void setData(JsonElement data) {
		this.data = data;
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

	public String getChildNamespace() {
		return childNamespace;
	}

	public void setChildNamespace(String childNamespace) {
		this.childNamespace = childNamespace;
	}

	public String getChildNamesapacePrefix() {
		return childNamesapacePrefix;
	}

	public void setChildNamesapacePrefix(String childNamesapacePrefix) {
		this.childNamesapacePrefix = childNamesapacePrefix;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

}