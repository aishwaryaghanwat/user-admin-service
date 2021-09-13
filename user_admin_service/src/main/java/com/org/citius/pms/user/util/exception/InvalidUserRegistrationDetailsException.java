package com.org.citius.pms.user.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, value = HttpStatus.BAD_REQUEST)
public class InvalidUserRegistrationDetailsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidUserRegistrationDetailsException(String errorMessage) {
		super(errorMessage);
	}

	public InvalidUserRegistrationDetailsException() {
		super();
	}

}
