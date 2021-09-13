package com.org.citius.pms.user.controller;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.org.citius.pms.user.controller.dto.ForgotUserPassword;
import com.org.citius.pms.user.controller.dto.Login;
import com.org.citius.pms.user.controller.dto.RegisterUserDetails;
import com.org.citius.pms.user.controller.dto.UserInformation;
import com.org.citius.pms.user.service.UserService;
import com.org.citius.pms.user.util.exception.CountryNotFoundException;
import com.org.citius.pms.user.util.exception.FirstTimeLoginException;
import com.org.citius.pms.user.util.exception.InvalidUserPasswordException;
import com.org.citius.pms.user.util.exception.InvalidUserRegistrationDetailsException;
import com.org.citius.pms.user.util.exception.UserAccountDeletedException;
import com.org.citius.pms.user.util.exception.UserAccountDisabledException;
import com.org.citius.pms.user.util.exception.UserAccountLockException;
import com.org.citius.pms.user.util.exception.UserAlreadyExistsException;
import com.org.citius.pms.user.util.exception.UserNotFoundException;
import com.org.citius.pms.user.util.exception.UserRoleNotFoundException;
import com.org.citius.pms.user.util.exception.UserStatusNotFoundException;
import com.org.citius.pms.user.util.exception.handler.response.ApplicationResponse;

import freemarker.template.TemplateException;

@RestController
@CrossOrigin("*")
public class PublicRequestsController {

	private static final Logger LOGGER = LoggerFactory.getLogger(PublicRequestsController.class);

	@Autowired
	private UserService userService;

	@PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public UserInformation login(@RequestBody @Valid Login user) throws IllegalArgumentException, UserNotFoundException,
			InvalidUserPasswordException, UserAccountLockException, FirstTimeLoginException,
			UserStatusNotFoundException, UserAccountDisabledException, UserAccountDeletedException, Exception {
		LOGGER.info("PublicRequestsController ::  login user execution started");
		try {
			return this.userService.login(user);
		} finally {
			LOGGER.info("PublicRequestsController ::  login user execution completed");
		}
	}

	@PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ApplicationResponse register(@RequestBody @Valid RegisterUserDetails registerUser)
			throws IllegalArgumentException, InvalidUserRegistrationDetailsException, UserAlreadyExistsException,
			IllegalArgumentException, UserStatusNotFoundException, UserRoleNotFoundException,
			UserStatusNotFoundException, CountryNotFoundException, IOException, TemplateException, MessagingException,
			Exception {
		LOGGER.info("PublicRequestsController ::  register user execution started");
		try {
			return this.userService.register(registerUser);
		} finally {
			LOGGER.info("PublicRequestsController ::  register user execution completed");
		}
	}

	@PostMapping(path = "/forgotPassword", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ApplicationResponse forgot(@RequestBody @Valid ForgotUserPassword forgotUserPassword)
			throws IllegalArgumentException, UserNotFoundException, UserAccountDisabledException,
			UserAccountLockException, UserAccountDeletedException, InvalidUserPasswordException,
			UserStatusNotFoundException, RuntimeException, MessagingException, IOException, TemplateException,
			Exception {
		LOGGER.info("PublicRequestsController ::  forgot user password execution started");
		try {
			return this.userService.forgotPassword(forgotUserPassword);
		} finally {
			LOGGER.info("PublicRequestsController ::  forgot user password execution completed");
		}
	}
}
