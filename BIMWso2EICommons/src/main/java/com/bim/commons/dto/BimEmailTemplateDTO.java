package com.bim.commons.dto;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

public class BimEmailTemplateDTO {

	private String template;
	private Map<String, String> mergeVariables;

	public BimEmailTemplateDTO() { }
	
	public BimEmailTemplateDTO(String template) {
		this.template = template;
	}
	
	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public Map<String, String> getMergeVariables() {
		return mergeVariables;
	}

	public void setMergeVariables(Map<String, String> mergeVariables) {
		this.mergeVariables = mergeVariables;
	}

	public void addMergeVariable(String key, String value) {
		if(this.mergeVariables == null)
			this.mergeVariables = new HashMap<String, String>();
		
		this.mergeVariables.put(key, value);
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
	
}
