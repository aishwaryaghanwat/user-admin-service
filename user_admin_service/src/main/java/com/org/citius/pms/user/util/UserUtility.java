package com.org.citius.pms.user.util;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.org.citius.pms.user.controller.dto.Login;
import com.org.citius.pms.user.controller.dto.RegisterUserDetails;
import com.org.citius.pms.user.service.CountryService;
import com.org.citius.pms.user.service.EmailService;
import com.org.citius.pms.user.service.TitleService;
import com.org.citius.pms.user.service.UserRoleService;
import com.org.citius.pms.user.service.UserService;
import com.org.citius.pms.user.service.UserStatusService;
import com.org.citius.pms.user.service.dao.User;
import com.org.citius.pms.user.service.dao.UserRole;
import com.org.citius.pms.user.service.dao.UserStatus;
import com.org.citius.pms.user.util.constants.AppConstants;
import com.org.citius.pms.user.util.exception.CountryNotFoundException;
import com.org.citius.pms.user.util.exception.FirstTimeLoginException;
import com.org.citius.pms.user.util.exception.InvalidUserRegistrationDetailsException;
import com.org.citius.pms.user.util.exception.UserAccountDeletedException;
import com.org.citius.pms.user.util.exception.UserAccountDisabledException;
import com.org.citius.pms.user.util.exception.UserAccountLockException;
import com.org.citius.pms.user.util.exception.UserAccountPasswordExpiredException;
import com.org.citius.pms.user.util.exception.UserAlreadyExistsException;
import com.org.citius.pms.user.util.exception.UserRoleNotFoundException;
import com.org.citius.pms.user.util.exception.UserStatusNotFoundException;
import com.org.citius.pms.user.util.exception.handler.response.ApplicationResponse;

import freemarker.template.TemplateException;

@Component
public class UserUtility {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserUtility.class);

	@Autowired
	private Hmac512PasswordEncoder hMac512PasswordEncoder;

	@Autowired
	private UserRoleService userRoleService;

	@Autowired
	private TitleService titleService;

	@Autowired
	private CountryService countryService;

	@Autowired
	private UserStatusService userStatusService;

	@Autowired
	private UserService userService;

	@Autowired
	private EmailService emailService;

	public void sendEmail(User user, String subject, String action)
			throws IOException, TemplateException, MessagingException {
		// send email
		Map<String, Object> templateModel = new HashMap<>();
		templateModel.put("name", ((Objects.nonNull(user.getFirstName()) ? user.getFirstName() : " ") + " "
				+ (Objects.nonNull(user.getLastName()) ? user.getLastName() : " ")));
		if (AppConstants.ACTION_FORGOT_PASSWORD.equals(action)) {
			this.emailService.sendMessageUsingFreemarkerTemplate(
					(Objects.nonNull(user.getEmailId()) ? user.getEmailId() : " "),
					Objects.nonNull(subject) ? subject : " ", templateModel, "forgotPassword.html");
		} else if (AppConstants.ACTION_FIRST_TIME_LOGIN_PASSWORD.equals(action)) {
			this.emailService.sendMessageUsingFreemarkerTemplate(
					(Objects.nonNull(user.getEmailId()) ? user.getEmailId() : " "),
					Objects.nonNull(subject) ? subject : " ", templateModel, "defaultPassword.html");
		} else if (AppConstants.ACTION_UNLOCK_USER.equals(action)) {
			this.emailService.sendMessageUsingFreemarkerTemplate(
					(Objects.nonNull(user.getEmailId()) ? user.getEmailId() : " "),
					Objects.nonNull(subject) ? subject : " ", templateModel, "unlockUser.html");
		} else if (AppConstants.ACTION_ENABLE_USER.equals(action)) {
			this.emailService.sendMessageUsingFreemarkerTemplate(
					(Objects.nonNull(user.getEmailId()) ? user.getEmailId() : " "),
					Objects.nonNull(subject) ? subject : " ", templateModel, "enableUser.html");
		}
	}

	public boolean validatePasswordExpiry(User user) {
		Boolean result = false;
		if (Objects.nonNull(user) && Objects.nonNull(user.getPasswrdExpiryDateTime())) {
			result = LocalDateTime.now().atOffset(ZoneOffset.UTC)
					.isAfter(user.getPasswrdExpiryDateTime().atOffset(ZoneOffset.UTC)) ? false : true;
		}
		return result;
	}

	public boolean validateUserPassword(Login loginUser, User user) {
		Boolean result = false;
		String encryptedLoginPassword = "";
		if (Objects.nonNull(loginUser.getPasswrd())) {
			encryptedLoginPassword = this.encryptPassword(loginUser.getPasswrd());
		}
		result = validateUserEncryptedPassword(encryptedLoginPassword,
				Objects.nonNull(user.getPasswrd()) ? user.getPasswrd() : "");
		return result;
	}

	public boolean validateUserEncryptedPassword(String encryptedEnteredPassword, String encryptedDataBasePassword) {
		Boolean result = false;
		if (Objects.nonNull(encryptedDataBasePassword) && Objects.nonNull(encryptedEnteredPassword)) {
			if (encryptedEnteredPassword.equals(encryptedDataBasePassword))
				result = true;
		}
		return result;
	}

	public boolean validUserAccount(User user) throws UserAccountLockException, FirstTimeLoginException,
			UserAccountDisabledException, UserAccountDeletedException {
		Boolean result = true;
		if (Objects.nonNull(user) && Objects.nonNull(user.getUserStatus())
				&& Objects.nonNull(user.getUserStatus().getId()) && !(user.getUserStatus().getId() == 1)) {
			if (user.getUserStatus().getId() == 2)
				throw new UserAccountLockException(AppConstants.ERROR_MESSAGE_ACCOUNT_LOCK);
			else if (user.getUserStatus().getId() == 3) {
				throw new UserAccountDisabledException(AppConstants.ERROR_MESSAGE_ACCOUNT_DISABLE);
			} else if (user.getUserStatus().getId() == 4) {
				throw new UserAccountDeletedException(AppConstants.ERROR_MESSAGE_ACCOUNT_DELETED);
			} else if (user.getUserStatus().getId() == 5) {
				throw new UserAccountPasswordExpiredException(AppConstants.ERROR_MESSAGE_ACCOUNT_PASSWORD_EXPIRED);
			}
		}
		return result;
	}

	public ApplicationResponse successApplicationResponse() {
		final ApplicationResponse response = new ApplicationResponse(LocalDateTime.now(ZoneOffset.UTC),
				AppConstants.SUCCESS, AppConstants.SUCCESS, HttpStatus.OK.value(), "");
		return response;
	}

	public boolean validateOldPassword(String oldPasswrd, String passwrd) {
		Boolean result = false;
		String encryptedEnteredPassword = "";
		if (Objects.nonNull(oldPasswrd)) {
			encryptedEnteredPassword = this.encryptPassword(oldPasswrd);
		}
		result = validateUserEncryptedPassword(encryptedEnteredPassword, Objects.nonNull(passwrd) ? passwrd : "");
		return result;
	}

	public boolean validateOldAndNewPassword(String oldPasswrd, String newPasswrd) {
		Boolean result = false;
		result = validateUserEncryptedPassword(oldPasswrd, newPasswrd);
		return result;
	}

	public String encryptPassword(String passwrd) {
		String encodedRawPass = (this.hMac512PasswordEncoder.encode(passwrd))
				.replace(Hmac512PasswordEncoder.SSHA512_PREFIX, "");
		return encodedRawPass;
	}

	public UserStatus getUserStatus(String status) throws IllegalArgumentException, UserStatusNotFoundException {
		return this.userStatusService.queryByStatusCodeName(status);
	}

	public UserRole getUserRole(String roleName) throws IllegalArgumentException, UserRoleNotFoundException {
		return this.userRoleService.queryByRoleName(roleName);
	}

	public boolean validateNewAndConfirmPassword(String newPasswrd, String confirmPasswrd) {
		Boolean result = false;
		if (Objects.nonNull(newPasswrd) && Objects.nonNull(confirmPasswrd)) {
			if (newPasswrd.equals(confirmPasswrd))
				return true;
		}
		return result;
	}

	public ApplicationResponse successApplicationResponse(String message) {
		final ApplicationResponse response = new ApplicationResponse(LocalDateTime.now(ZoneOffset.UTC), message,
				AppConstants.SUCCESS, HttpStatus.OK.value(), "");
		return response;
	}

	public String createDefaultPassword(User user) {
		StringBuilder defaultPassword = new StringBuilder();
		// defaultPassword = Default2021
		defaultPassword
				.append(Objects.nonNull(user.getFirstName()) ? user.getFirstName().toUpperCase().charAt(0) : "D");
		defaultPassword.append(
				Objects.nonNull(user.getFirstName()) ? user.getFirstName().toLowerCase().subSequence(1, 4) : "efault");
		defaultPassword.append(Objects.nonNull(user.getDob()) ? user.getDob().getYear() : "2021");
		return this.encryptPassword(defaultPassword.toString());
	}

	public User validateAndFillUserDetailsToRegister(RegisterUserDetails registerUser)
			throws InvalidUserRegistrationDetailsException, UserAlreadyExistsException, IllegalArgumentException,
			UserStatusNotFoundException, UserRoleNotFoundException, UserStatusNotFoundException,
			CountryNotFoundException {
		User user = null;
		if (Objects.nonNull(registerUser)) {
			user = new User();

			if (Objects.nonNull(registerUser.getUserRole())) {
				user.setUserRole(this.userRoleService.queryByRoleName(registerUser.getUserRole()));
			}
			if (Objects.nonNull(registerUser.getTitle())) {
				user.setTitle(this.titleService.queryByTitleName(registerUser.getTitle()));
			}
			if (Objects.nonNull(registerUser.getCountryCode())) {
				user.setCountry(this.countryService.queryByCountryCode(registerUser.getCountryCode()));
			}
			if (registerUser.getFirstName() != null && registerUser.getLastName() != null) {
				user.setFirstName(registerUser.getFirstName());
				user.setLastName(registerUser.getLastName());
			} else {
				throw new InvalidUserRegistrationDetailsException(AppConstants.ERROR_MESSAGE_INVALID_FIRST_LAST_NAME);
			}
			if (registerUser.getEmailId() != null) {
				if (!(this.userService.existsByEmailId(registerUser.getEmailId())))
					user.setEmailId(registerUser.getEmailId());
				else
					throw new UserAlreadyExistsException(AppConstants.ERROR_MESSAGE_CONFLICT_USER_EMAIL_EXIST);
			} else {
				throw new InvalidUserRegistrationDetailsException(AppConstants.ERROR_MESSAGE_INVALID_EMAIL);
			}
			if (registerUser.getContactNumber() != null) {
				if (!(this.userService.existsByContactNumber(registerUser.getContactNumber())))
					user.setContactNumber(Long.valueOf(registerUser.getContactNumber()));
				else
					throw new UserAlreadyExistsException(AppConstants.ERROR_MESSAGE_CONFLICT_USER_CONTACT_EXIST);
			} else {
				throw new InvalidUserRegistrationDetailsException(AppConstants.ERROR_MESSAGE_INVALID_CONTACT);
			}
			if (registerUser.getGender() != null) {
				user.setGender(registerUser.getGender());
			} else {
				throw new InvalidUserRegistrationDetailsException(AppConstants.ERROR_MESSAGE_INVALID_GENDER);
			}

			if (registerUser.getDob() != null) {
				LocalDate dob = LocalDate.parse(registerUser.getDob());
				if (dob.isBefore(LocalDate.now()))
					user.setDob(dob);
				else
					throw new InvalidUserRegistrationDetailsException(AppConstants.ERROR_MESSAGE_INVALID_DOB);
			} else {
				throw new InvalidUserRegistrationDetailsException(AppConstants.ERROR_MESSAGE_INVALID_DOB);
			}

			user.setInvalidRetryCount(0);
			user.setUserStatus(this.getUserStatus(AppConstants.USER_ACCOUNT_ACTIVE));
			user.setCreatedBy(Objects.nonNull(registerUser.getCreatedBy()) ? registerUser.getCreatedBy() : null);

			if (registerUser.getCreatedDateTime() != null) {
				try {
					user.setCreatedDateTime(
							Objects.nonNull(registerUser.getCreatedDateTime())
									? LocalDateTime
											.parse(registerUser.getCreatedDateTime(),
													DateTimeFormatter.ofPattern(AppConstants.LOCAL_DATE_TIME_FORMAT,
															Locale.ENGLISH))
											.atOffset(ZoneOffset.UTC).toLocalDateTime()
									: null);
				} catch (DateTimeParseException ex) {
					throw new InvalidUserRegistrationDetailsException(
							AppConstants.ERROR_MESSAGE_INVALID_CREATED_DATE_TIME);
				}
			} else {
				user.setCreatedDateTime(LocalDateTime.now().atOffset(ZoneOffset.UTC).toLocalDateTime());
			}

		} else {
			throw new InvalidUserRegistrationDetailsException(AppConstants.ERROR_MESSAGE_USER_INVALID_DETAILS);
		}

		return user;
	}

}
