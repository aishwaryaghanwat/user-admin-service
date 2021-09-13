package com.org.citius.pms.user.util.exception.handler.response;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.org.citius.pms.user.util.constants.AppConstants;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty(value = "dateTime")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConstants.LOCAL_DATE_TIME_FORMAT)
	private LocalDateTime dateTime;

	@JsonProperty(value = "status")
	private String status;

	@JsonProperty(value = "statusCode")
	private int statusCode;

	@JsonProperty(value = "message")
	private String message;

	@JsonProperty(value = "errors")
	private List<String> errors;

	public ApplicationResponse(LocalDateTime dateTime, String message, String status, int statusCode,
			List<String> errors) {
		super();
		this.dateTime = dateTime;
		this.message = message;
		this.status = status;
		this.statusCode = statusCode;
		this.errors = errors;
	}

	public ApplicationResponse(LocalDateTime dateTime, String message, String status, int statusCode, String error) {
		super();
		this.dateTime = dateTime;
		this.message = message;
		this.status = status;
		this.statusCode = statusCode;
		this.errors = Arrays.asList(error);
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
