package com.org.citius.pms.user.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, value = HttpStatus.NOT_FOUND)
public class UserStatusNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserStatusNotFoundException(String errorMessage) {
		super(errorMessage);
	}

	public UserStatusNotFoundException() {
		super();
	}

}