package com.xjj.http;

public class HttpHelperResult {
	private int code;
	private String msg;
	
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	@Override
	public String toString() {
		return "HttpHelperResult [code=" + code + ", msg=" + msg + "]";
	}
	
	
}
