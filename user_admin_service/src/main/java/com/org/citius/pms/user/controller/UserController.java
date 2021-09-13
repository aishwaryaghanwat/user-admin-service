package com.org.citius.pms.user.controller;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.org.citius.pms.user.controller.dto.UserInformation;
import com.org.citius.pms.user.service.UserService;
import com.org.citius.pms.user.util.exception.UserNotFoundException;
import com.org.citius.pms.user.util.exception.handler.response.ApplicationResponse;

import freemarker.template.TemplateException;

@RestController
@RequestMapping(path = "/user")
@CrossOrigin("*")
public class UserController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@GetMapping(path = "/{id}")
	@ResponseBody
	public UserInformation findUserById(@PathVariable("id") String id)
			throws IllegalArgumentException, UserNotFoundException {
		LOGGER.info("UserController ::  findUserById execution started");
		try {
			if (Pattern.matches("[0-9]+", id)) {
				return this.userService.findUserById(Long.valueOf(id));
			} else {
				return this.userService.findUserByUserName(id);
			}
		} finally {
			LOGGER.info("UserController ::  findUserById execution completed");
		}
	}

	@DeleteMapping(path = "/{id}")
	@ResponseBody
	public ApplicationResponse deleteUserById(@PathVariable("id") String userId)
			throws IllegalArgumentException, UserNotFoundException, IOException, TemplateException, MessagingException {
		LOGGER.info("UserController ::  deleteUserById execution started");
		try {
			if (Pattern.matches("[0-9]+", userId)) {
				return this.userService.deleteUserByUserId(null, Long.valueOf(userId));
			} else {
				return this.userService.deleteUserByUserName(null, userId);
			}
		} finally {
			LOGGER.info("UserController ::  deleteUserById execution completed");
		}
	}

}
