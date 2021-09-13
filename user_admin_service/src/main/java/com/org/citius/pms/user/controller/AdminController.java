package com.org.citius.pms.user.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.org.citius.pms.user.controller.dto.RegisterUserDetails;
import com.org.citius.pms.user.controller.dto.UserInformation;
import com.org.citius.pms.user.service.UserService;
import com.org.citius.pms.user.service.dao.User;
import com.org.citius.pms.user.service.dao.specifications.UserSpecs;
import com.org.citius.pms.user.util.UserUtility;
import com.org.citius.pms.user.util.builder.SpecificationsBuilder;
import com.org.citius.pms.user.util.constants.AppConstants;
import com.org.citius.pms.user.util.exception.CountryNotFoundException;
import com.org.citius.pms.user.util.exception.InvalidUserRegistrationDetailsException;
import com.org.citius.pms.user.util.exception.UserAlreadyExistsException;
import com.org.citius.pms.user.util.exception.UserNotFoundException;
import com.org.citius.pms.user.util.exception.UserRoleNotFoundException;
import com.org.citius.pms.user.util.exception.UserStatusNotFoundException;
import com.org.citius.pms.user.util.exception.handler.response.ApplicationResponse;
import com.org.citius.pms.user.util.mapper.UserToUserInformationDtoMapper;

import freemarker.template.TemplateException;

@RestController
@RequestMapping(path = "/admin")
@CrossOrigin("*")
public class AdminController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private UserToUserInformationDtoMapper userToUserInformationDtoMapper;

	@Autowired
	private UserUtility userUtility;

	@GetMapping(path = "/{adminId}/user/list")
	@ResponseBody
	public Iterable<UserInformation> listUsers(@PathVariable("adminId") Long adminId,
			@RequestParam(required = false, value = "page") Integer page,
			@RequestParam(required = false, value = "page_size") Integer pageSize,
			@RequestParam(required = false, value = "sort_props[]") String[] sortProperties,
			@RequestParam(required = false, value = "sort_asc", defaultValue = "false") boolean sortAscending,
			@RequestParam(required = false, value = "lastName") String lastName,
			@RequestParam(required = false, value = "firstName") String firstName,
			@RequestParam(required = false, value = "userStatus") String userStatus,
			@RequestParam(required = false, value = "userRole") String userRole,
			@RequestParam(required = false, value = "username") String emailId,
			@RequestParam(required = false, value = "id") Long id) throws IllegalArgumentException {

		LOGGER.info("AdminController ::  register user execution started");
		try {
			if (this.userService.validateAdminUser(adminId)) {
				SpecificationsBuilder<User> specificationsBuilder = new SpecificationsBuilder<>();
				specificationsBuilder.addSpecification(UserSpecs.filterById((Objects.nonNull(id)) ? id : null));
				specificationsBuilder
						.addSpecification(UserSpecs.filterByFirstName((Objects.nonNull(firstName)) ? firstName : null));
				specificationsBuilder
						.addSpecification(UserSpecs.filterByLastName((Objects.nonNull(lastName)) ? lastName : null));
				specificationsBuilder
						.addSpecification(UserSpecs.filterByEmailId((Objects.nonNull(emailId)) ? emailId : null));
				specificationsBuilder.addSpecification(UserSpecs.filterByUserStatus(Objects.nonNull(userStatus)
						? Optional.ofNullable(this.userUtility.getUserStatus(userStatus)).orElseThrow(null)
						: null));
				specificationsBuilder.addSpecification(UserSpecs.filterByUserRole(Objects.nonNull(userRole)
						? Optional.ofNullable(this.userUtility.getUserRole(userRole)).orElseThrow(null)
						: null));

				if (Objects.nonNull(page) && page >= 0 && Objects.nonNull(pageSize) && pageSize > 0) {
					PageRequest pageRequest = PageRequest.of(page, pageSize,
							sortAscending ? Direction.ASC : Direction.DESC,
							Optional.ofNullable(sortProperties)
									.map(items -> Arrays.stream(items).filter(StringUtils::isNotBlank))
									.orElse(Stream.of("createdDateTime")).toArray(String[]::new));
					Page<User> users = this.userService.getAllUsers(specificationsBuilder.build(), pageRequest);
					return users.map(this.userToUserInformationDtoMapper::convert);
				}

				Iterable<User> usersList = this.userService.getAllUsers(specificationsBuilder.build());
				Iterable<UserInformation> userDtoList = StreamSupport.stream(usersList.spliterator(), false)
						.map(this.userToUserInformationDtoMapper::convert).collect(Collectors.toList());
				Collections.sort((List<UserInformation>) userDtoList);
				return Objects.nonNull(userDtoList) ? userDtoList : null;
			} else {
				throw new UserNotFoundException(AppConstants.ERROR_MESSAGE_INVALID_ADMIN_USER);
			}
		} finally {
			LOGGER.info("AdminController ::  register user execution completed");
		}
	}

	@PostMapping(path = "/{adminId}/user/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ApplicationResponse register(@PathVariable("adminId") Long adminId,
			@RequestBody @Valid RegisterUserDetails registerUser) throws IllegalArgumentException,
			InvalidUserRegistrationDetailsException, UserAlreadyExistsException, IllegalArgumentException,
			UserStatusNotFoundException, UserRoleNotFoundException, UserStatusNotFoundException,
			CountryNotFoundException, IOException, TemplateException, MessagingException, Exception {
		LOGGER.info("AdminController ::  register user execution started");
		try {
			if (this.userService.validateAdminUser(adminId)) {
				return this.userService.register(registerUser);
			} else {
				throw new UserNotFoundException(AppConstants.ERROR_MESSAGE_INVALID_ADMIN_USER);
			}
		} finally {
			LOGGER.info("AdminController ::  register user execution completed");
		}
	}

	@PutMapping(path = "/{adminId}/user/{userId}/active", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ApplicationResponse enableUser(@PathVariable("adminId") Long adminId, @PathVariable("userId") String userId)
			throws IllegalArgumentException, UserNotFoundException, IOException, TemplateException, MessagingException {
		LOGGER.info("AdminController ::  enableUser execution started");
		try {
			if (this.userService.validateAdminUser(adminId)) {
				if (Pattern.matches("[0-9]+", userId)) {
					return this.userService.enableUserByUserId(adminId, Long.valueOf(userId));
				} else {
					return this.userService.enableUserByUserName(adminId, userId);
				}
			} else {
				throw new UserNotFoundException(AppConstants.ERROR_MESSAGE_INVALID_ADMIN_USER);
			}
		} finally {
			LOGGER.info("AdminController ::  enableUser execution completed");
		}
	}

	@PutMapping(path = "/{adminId}/user/{userId}/delete", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ApplicationResponse deleteUser(@PathVariable("adminId") Long adminId, @PathVariable("userId") String userId)
			throws IllegalArgumentException, UserNotFoundException, IOException, TemplateException, MessagingException {
		LOGGER.info("AdminController ::  deleteUser execution started");
		try {
			if (this.userService.validateAdminUser(adminId)) {
				if (Pattern.matches("[0-9]+", userId)) {
					return this.userService.deleteUserByUserId(adminId, Long.valueOf(userId));
				} else {
					return this.userService.deleteUserByUserName(adminId, userId);
				}
			} else {
				throw new UserNotFoundException(AppConstants.ERROR_MESSAGE_INVALID_ADMIN_USER);
			}
		} finally {
			LOGGER.info("AdminController ::  deleteUser execution completed");
		}
	}

	@PutMapping(path = "/{adminId}/user/{userId}/disable", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ApplicationResponse disableUser(@PathVariable("adminId") Long adminId,
			@PathVariable("userId") String userId)
			throws IllegalArgumentException, UserNotFoundException, IOException, TemplateException, MessagingException {
		LOGGER.info("AdminController ::  disableUser execution started");
		try {
			if (this.userService.validateAdminUser(adminId)) {
				if (Pattern.matches("[0-9]+", userId)) {
					return this.userService.disableUserByUserId(adminId, Long.valueOf(userId));
				} else {
					return this.userService.disableUserByUserName(adminId, userId);
				}
			} else {
				throw new UserNotFoundException(AppConstants.ERROR_MESSAGE_INVALID_ADMIN_USER);
			}
		} finally {
			LOGGER.info("AdminController ::  disableUser execution completed");
		}
	}
}
