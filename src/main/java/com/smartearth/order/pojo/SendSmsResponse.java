package com.smartearth.order.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SendSmsResponse {
	
	@JsonProperty("Code")
	private String Code;

	public String getCode() {
		return Code;
	}

	public void setCode(String code) {
		Code = code;
	}
}
