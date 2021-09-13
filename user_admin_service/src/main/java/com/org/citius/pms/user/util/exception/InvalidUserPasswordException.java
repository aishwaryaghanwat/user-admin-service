package com.org.citius.pms.user.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, value = HttpStatus.BAD_REQUEST, reason = "Bad Request - Invalid user Credentials....")
public class InvalidUserPasswordException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidUserPasswordException(String errorMessage) {
		super(errorMessage);
	}

	public InvalidUserPasswordException() {
		super();
	}

}
