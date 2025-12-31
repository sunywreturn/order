package com.smartearth.order.pojo;

public class Response {
	private int status;
	private String msg;
	private Object data;
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public static Response getSuccessResult() {
		Response response = new Response();
		response.setStatus(200);
		response.setMsg("success");
		return response;
	}

	public static Response getFailResult() {
		Response response = new Response();
		response.setStatus(500);
		response.setMsg("fail");
		return response;
	}
}
