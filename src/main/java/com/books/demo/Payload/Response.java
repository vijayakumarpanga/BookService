package com.books.demo.Payload;

import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


@JsonInclude(Include.NON_NULL)
public class Response {
	
	
	private String status;
	private HttpStatus statusCode;
	private List<JSONObject> data;
	private String errMessage;
	
	public Response(String status, HttpStatus statusCode, List<JSONObject> data, String errMessage) {
		super();
		this.status = status;
		this.statusCode = statusCode;
		this.data = data;
		this.errMessage = errMessage;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public HttpStatus getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(HttpStatus statusCode) {
		this.statusCode = statusCode;
	}

	public List<JSONObject> getData() {
		return data;
	}

	public void setData(List<JSONObject> data) {
		this.data = data;
	}

	public String getErrMessage() {
		return errMessage;
	}

	public void setErrMessage(String errMessage) {
		this.errMessage = errMessage;
	}
	
		
}
