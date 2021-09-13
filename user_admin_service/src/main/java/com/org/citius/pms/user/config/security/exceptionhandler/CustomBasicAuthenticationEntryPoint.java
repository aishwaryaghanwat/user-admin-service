package com.org.citius.pms.user.config.security.exceptionhandler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.citius.pms.user.util.constants.AppConstants;
import com.org.citius.pms.user.util.exception.handler.response.ApplicationResponse;

public class CustomBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

	// 401
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {
		response.addHeader("WWW-Authenticate", "Basic realm=\"" + this.getRealmName() + "\"");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		final ApplicationResponse apiError = new ApplicationResponse(null,
				"Authentication Failure, Kindly provide correct basic authentication to access the api",
				AppConstants.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), "");
		response.getWriter().write(new ObjectMapper().writeValueAsString(apiError));
	}

}
