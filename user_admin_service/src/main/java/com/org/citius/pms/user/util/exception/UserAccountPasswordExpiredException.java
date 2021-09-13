package com.org.citius.pms.user.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY, value = HttpStatus.UNPROCESSABLE_ENTITY)
public class UserAccountPasswordExpiredException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserAccountPasswordExpiredException(String errorMessage) {
		super(errorMessage);
	}

	public UserAccountPasswordExpiredException() {
		super();
	}

}
