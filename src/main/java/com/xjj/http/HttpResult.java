package com.xjj.http;

/**
 * Created by XuJijun on 2017-06-06.
 */
public class HttpResult {
	private int status;
	private String payload;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}
}
