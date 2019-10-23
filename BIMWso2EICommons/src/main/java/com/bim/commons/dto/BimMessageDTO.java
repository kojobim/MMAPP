package com.bim.commons.dto;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

public class BimMessageDTO {

	private String code;
	private Map<String, String> mergeVariables;

	public BimMessageDTO() { }
	
	public BimMessageDTO(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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
