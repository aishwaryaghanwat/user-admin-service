package com.org.citius.pms.user.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.org.citius.pms.user.controller.dto.ForgotUserPassword;
import com.org.citius.pms.user.controller.dto.Login;
import com.org.citius.pms.user.controller.dto.PasswordForgotType;
import com.org.citius.pms.user.controller.dto.RegisterUserDetails;
import com.org.citius.pms.user.controller.dto.UserInformation;
import com.org.citius.pms.user.repository.UserRepository;
import com.org.citius.pms.user.service.dao.User;
import com.org.citius.pms.user.util.UserUtility;
import com.org.citius.pms.user.util.constants.AppConstants;
import com.org.citius.pms.user.util.exception.CountryNotFoundException;
import com.org.citius.pms.user.util.exception.FirstTimeLoginException;
import com.org.citius.pms.user.util.exception.InvalidUserPasswordException;
import com.org.citius.pms.user.util.exception.InvalidUserRegistrationDetailsException;
import com.org.citius.pms.user.util.exception.UserAccountDeletedException;
import com.org.citius.pms.user.util.exception.UserAccountDisabledException;
import com.org.citius.pms.user.util.exception.UserAccountLockException;
import com.org.citius.pms.user.util.exception.UserAccountPasswordExpiredException;
import com.org.citius.pms.user.util.exception.UserAlreadyExistsException;
import com.org.citius.pms.user.util.exception.UserNotFoundException;
import com.org.citius.pms.user.util.exception.UserRoleNotFoundException;
import com.org.citius.pms.user.util.exception.UserStatusNotFoundException;
import com.org.citius.pms.user.util.exception.handler.response.ApplicationResponse;
import com.org.citius.pms.user.util.mapper.UserToUserInformationDtoMapper;

import freemarker.template.TemplateException;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserToUserInformationDtoMapper userToUserInformationDtoMapper;

	@Autowired
	private UserUtility userUtility;

	@Transactional(readOnly = true)
	public User queryUserName(String username) throws IllegalArgumentException, UserNotFoundException {
		if (Objects.isNull(username))
			throw new IllegalArgumentException("Username cannot be null / empty...");
		return Optional.ofNullable(this.userRepository.findByEmailId(username))
				.orElseThrow(() -> new UserNotFoundException(AppConstants.ERROR_MESSAGE_USER_ACCOUNT_NOT_FOUND));
	}

	@Transactional(readOnly = true)
	public Optional<User> queryUserId(Long id) throws IllegalArgumentException, UserNotFoundException {
		if (Objects.isNull(id))
			throw new IllegalArgumentException("User id cannot be null / empty...");
		return Optional.ofNullable(this.userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException(AppConstants.ERROR_MESSAGE_USER_ID_NOT_FOUND)));
	}

	@Transactional(readOnly = true)
	public Boolean existsByEmailId(String username) throws IllegalArgumentException {
		if (Objects.isNull(username))
			throw new IllegalArgumentException("Username / Email ID cannot be null / empty...");
		return this.userRepository.existsByEmailId(username);
	}

	@Transactional(readOnly = true)
	public Boolean existsByContactNumber(String contactNumber) throws IllegalArgumentException {
		if (Objects.isNull(contactNumber))
			throw new IllegalArgumentException("Contact number cannot be null / empty...");
		return this.userRepository.existsByContactNumber(Long.valueOf(contactNumber));
	}

	@Transactional(readOnly = true)
	public Boolean existsByEmployeeID(String employeeID) throws IllegalArgumentException {
		if (Objects.isNull(employeeID))
			throw new IllegalArgumentException("Employee ID cannot be null / empty...");
		return this.userRepository.existsByEmployeeID(employeeID);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Iterable<User> getAllUsers(Specification<User> specification) {
		return this.userRepository.findAll(specification);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Page<User> getAllUsers(Specification<User> specification, Pageable pageable) {
		return this.userRepository.findAll(specification, pageable);
	}

	public UserInformation login(Login loginUser) throws IllegalArgumentException, UserNotFoundException,
			InvalidUserPasswordException, UserAccountLockException, FirstTimeLoginException,
			UserStatusNotFoundException, UserAccountDisabledException, UserAccountDeletedException {

		User user = queryUserName(loginUser.getUserName());
		if (this.userUtility.validUserAccount(user)) {
			if (!this.userUtility.validatePasswordExpiry(user)) {
				user.setUserStatus(this.userUtility.getUserStatus(AppConstants.USER_ACCOUNT_PASSWORD_EXPIRED));
				this.userRepository.save(user);
				throw new UserAccountPasswordExpiredException(AppConstants.ERROR_MESSAGE_ACCOUNT_PASSWORD_EXPIRED);
			} else if (!this.userUtility.validateUserPassword(loginUser, user)) {
				String messsage = "";
				if (user.getInvalidRetryCount() == 0) {
					messsage = "Bad request - 2 login attempt is remaining";
				} else if (user.getInvalidRetryCount() == 1) {
					messsage = "Bad request - 1 login attempt is remaining";
				} else {
					messsage = "Bad request - Your account has been locked. Please contact the hospital administrator or call helpdesk on 123456 for more information.";
					user.setUserStatus(this.userUtility.getUserStatus(AppConstants.USER_ACCOUNT_LOCKED));
				}
				user.setInvalidRetryCount((user.getInvalidRetryCount() + 1));
				this.userRepository.save(user);
				throw new InvalidUserPasswordException(messsage);
			} else if (Objects.nonNull(user) && Objects.nonNull(user.getIsFirstTimeLogin())
					&& user.getIsFirstTimeLogin() == true) {
				throw new FirstTimeLoginException(AppConstants.ERROR_MESSAGE_FIRST_TIME_LOGIN);
			} else {
				user.setInvalidRetryCount(0);
				user.setIsFirstTimeLogin(false);
				this.userRepository.save(user);
				return this.userToUserInformationDtoMapper.convert(user); // dao to dto mapping
			}
		}
		return null;
	}

	public ApplicationResponse register(RegisterUserDetails registerUser)
			throws InvalidUserRegistrationDetailsException, UserAlreadyExistsException, IllegalArgumentException,
			UserStatusNotFoundException, UserRoleNotFoundException, UserStatusNotFoundException,
			CountryNotFoundException, IOException, TemplateException, MessagingException {

		User user = this.userUtility.validateAndFillUserDetailsToRegister(registerUser);

		if (Objects.nonNull(registerUser) && Objects.nonNull(registerUser.getUserRole())
				&& AppConstants.PATIENT.equals(registerUser.getUserRole())) {
			if (this.userUtility.validateNewAndConfirmPassword(registerUser.getPasswrd(),
					registerUser.getConfirmPasswrd())) {
				user.setPasswrd(this.userUtility.encryptPassword(registerUser.getPasswrd()));
				user.setPasswrdExpiryDateTime(LocalDateTime.now().atOffset(ZoneOffset.UTC)
						.plusDays(AppConstants.ADD_PASSWORD_EXPIRY_45_DAYS).toLocalDateTime());
			} else {
				throw new InvalidUserPasswordException(AppConstants.ERROR_MESSAGE_INVALID_NEW_AND_CONFIRM_PASSWORD);
			}
			user.setIsFirstTimeLogin(false);
		} else {

			if (Objects.nonNull(registerUser.getEmployeeID())) {
				if (!(this.existsByEmployeeID(registerUser.getEmployeeID())))
					user.setEmployeeID(registerUser.getEmployeeID());
				else
					throw new UserAlreadyExistsException(AppConstants.ERROR_MESSAGE_CONFLICT_EMPLOYEE_EXIST);
			} else {
				throw new InvalidUserRegistrationDetailsException(AppConstants.ERROR_MESSAGE_INVALID_EMPLOYEE_ID);
			}
			user.setPasswrd(this.userUtility.createDefaultPassword(user));
			user.setPasswrdExpiryDateTime(LocalDateTime.now().atOffset(ZoneOffset.UTC)
					.plusHours(AppConstants.ADD_PASSWORD_EXPIRY_2_HOURS).toLocalDateTime());
			user.setIsFirstTimeLogin(true);

			// send email
			this.userUtility.sendEmail(user, AppConstants.FIRST_TIME_LOGIN_PASSWORD_FOR_PMS_PORTAL,
					AppConstants.ACTION_FIRST_TIME_LOGIN_PASSWORD);
		}

		this.userRepository.save(user);

		return this.userUtility.successApplicationResponse(AppConstants.USER_CREATED_SUCCSESSFULLY);
	}

	public ApplicationResponse forgotPassword(ForgotUserPassword forgotUserPassword)
			throws IllegalArgumentException, UserNotFoundException, UserAccountDisabledException,
			UserAccountLockException, UserAccountDeletedException, InvalidUserPasswordException,
			UserStatusNotFoundException, RuntimeException, MessagingException, IOException, TemplateException {

		if (Objects.nonNull(forgotUserPassword) && Objects.nonNull(forgotUserPassword.getPasswrdForgotType())) {
			User user = queryUserName(forgotUserPassword.getUserName());

			if (Objects.nonNull(user) && Objects.nonNull(user.getUserStatus())
					&& Objects.nonNull(user.getUserStatus().getId()) && !(user.getUserStatus().getId() == 1)) {
				if (user.getUserStatus().getId() == 2)
					throw new UserAccountLockException(AppConstants.ERROR_MESSAGE_ACCOUNT_LOCK);
				else if (user.getUserStatus().getId() == 3) {
					throw new UserAccountDisabledException(AppConstants.ERROR_MESSAGE_ACCOUNT_DISABLE);
				} else if (user.getUserStatus().getId() == 4) {
					throw new UserAccountDeletedException(AppConstants.ERROR_MESSAGE_ACCOUNT_DELETED);
				}
			}

			if (PasswordForgotType.FORGOT.getValue().equals(forgotUserPassword.getPasswrdForgotType())) {
				user.setPasswrd(this.userUtility.createDefaultPassword(user));
				user.setPasswrdExpiryDateTime(LocalDateTime.now().atOffset(ZoneOffset.UTC)
						.plusHours(AppConstants.ADD_PASSWORD_EXPIRY_2_HOURS).toLocalDateTime());
				user.setUserStatus(this.userUtility.getUserStatus(AppConstants.USER_ACCOUNT_ACTIVE));
				user.setInvalidRetryCount(0);
				user.setIsFirstTimeLogin(true);
				user.setModifiedBy(user.getId());
				user.setModifiedDateTime(LocalDateTime.now().atOffset(ZoneOffset.UTC).toLocalDateTime());

				// send email
				this.userUtility.sendEmail(user, AppConstants.FORGOT_PASSWORD_FOR_PMS_PORTAL,
						AppConstants.ACTION_FORGOT_PASSWORD);

				this.userRepository.save(user);

				return this.userUtility.successApplicationResponse(AppConstants.USER_PASSWORD_FORGOT_SUCCESS);

			} else {
				if (Objects.nonNull(user) && Objects.nonNull(user.getUserStatus())
						&& Objects.nonNull(user.getUserStatus().getId()) && !(user.getUserStatus().getId() == 1)) {
					if (user.getUserStatus().getId() == 5) {
						throw new UserAccountPasswordExpiredException(
								AppConstants.ERROR_MESSAGE_ACCOUNT_PASSWORD_EXPIRED);
					}
				}

				if (!(this.userUtility.validateOldPassword(forgotUserPassword.getOldPasswrd(), user.getPasswrd()))) {
					throw new InvalidUserPasswordException(AppConstants.ERROR_MESSAGE_IVALID_OLD_PASSWORD);
				}
				if (!this.userUtility.validatePasswordExpiry(user)) {
					user.setUserStatus(this.userUtility.getUserStatus(AppConstants.USER_ACCOUNT_PASSWORD_EXPIRED));
					this.userRepository.save(user);
					throw new UserAccountPasswordExpiredException(AppConstants.ERROR_MESSAGE_ACCOUNT_PASSWORD_EXPIRED);
				}
				if (this.userUtility.validateOldAndNewPassword(forgotUserPassword.getOldPasswrd(),
						forgotUserPassword.getNewPasswrd())) {
					throw new InvalidUserPasswordException(AppConstants.ERROR_MESSAGE_SAME_OLD_AND_NEW_PASSWORD);
				}
				if (!(this.userUtility.validateNewAndConfirmPassword(forgotUserPassword.getNewPasswrd(),
						forgotUserPassword.getConfirmPasswrd()))) {
					throw new InvalidUserPasswordException(AppConstants.ERROR_MESSAGE_INVALID_NEW_AND_CONFIRM_PASSWORD);
				}
				user.setPasswrd(this.userUtility.encryptPassword(forgotUserPassword.getNewPasswrd()));
				user.setPasswrdExpiryDateTime(LocalDateTime.now().atOffset(ZoneOffset.UTC)
						.plusDays(AppConstants.ADD_PASSWORD_EXPIRY_45_DAYS).toLocalDateTime());
				user.setUserStatus(this.userUtility.getUserStatus(AppConstants.USER_ACCOUNT_ACTIVE));
				user.setInvalidRetryCount(0);
				user.setIsFirstTimeLogin(false);
				user.setModifiedBy(user.getId());
				user.setModifiedDateTime(LocalDateTime.now().atOffset(ZoneOffset.UTC).toLocalDateTime());
				this.userRepository.save(user);
				return this.userUtility.successApplicationResponse(AppConstants.USER_PASSWORD_CHANGE_SUCCSESS);
			}
		}
		throw new RuntimeException(AppConstants.ERROR_MESSAGE_UNABLE_TO_PROCESS_REQUEST);
	}

	public boolean validateAdminUser(Long adminId) throws IllegalArgumentException, UserNotFoundException {
		Boolean result = false;
		try {
			Optional<User> user = queryUserId(adminId);
			if (Objects.nonNull(user.get())) {
				User userdetails = user.get();
				if (Objects.nonNull(userdetails.getUserRole())
						&& Objects.nonNull(userdetails.getUserRole().getRoleName())) {
					if (AppConstants.ADMIN.equals(userdetails.getUserRole().getRoleName())) {
						result = true;
					} else {
						throw new UserNotFoundException(AppConstants.ERROR_MESSAGE_INVALID_ADMIN_USER);
					}
				}
			}
		} catch (IllegalArgumentException | UserNotFoundException ex) {
			throw new UserNotFoundException(AppConstants.ERROR_MESSAGE_INVALID_ADMIN_USER);
		}
		return result;
	}

	public ApplicationResponse enableUserByUserName(Long adminID, String username)
			throws IllegalArgumentException, UserNotFoundException, IOException, TemplateException, MessagingException {
		// TODO Auto-generated method stub
		User user = queryUserName(username);
		if (Objects.nonNull(user)) {
			return activateUser(adminID, user, AppConstants.ENABLE_USER_FOR_PMS_PORTAL,
					AppConstants.ACTION_ENABLE_USER);
		} else {
			throw new UserNotFoundException(AppConstants.ERROR_MESSAGE_USER_ACCOUNT_NOT_FOUND);
		}
	}

	public ApplicationResponse enableUserByUserId(Long adminID, Long userid)
			throws IllegalArgumentException, UserNotFoundException, IOException, TemplateException, MessagingException {
		// TODO Auto-generated method stub
		Optional<User> user = queryUserId(userid);
		if (Objects.nonNull(user) && Objects.nonNull(user.get())) {
			return activateUser(adminID, user.get(), AppConstants.ENABLE_USER_FOR_PMS_PORTAL,
					AppConstants.ACTION_ENABLE_USER);
		} else {
			throw new UserNotFoundException(AppConstants.ERROR_MESSAGE_USER_ID_NOT_FOUND);
		}
	}

	public ApplicationResponse deleteUserByUserName(Long adminID, String username)
			throws IllegalArgumentException, UserNotFoundException, IOException, TemplateException, MessagingException {
		// TODO Auto-generated method stub
		User user = queryUserName(username);
		if (Objects.nonNull(user)) {
			return deleteOrDisableUser(adminID, user, AppConstants.USER_ACCOUNT_DELETED,
					AppConstants.USER_ACCOUNT_DELETE_SUCCSESS);
		} else {
			throw new UserNotFoundException(AppConstants.ERROR_MESSAGE_USER_ID_NOT_FOUND);
		}
	}

	public ApplicationResponse deleteUserByUserId(Long adminID, Long userid)
			throws IllegalArgumentException, UserNotFoundException, IOException, TemplateException, MessagingException {
		// TODO Auto-generated method stub
		Optional<User> user = queryUserId(userid);
		if (Objects.nonNull(user) && Objects.nonNull(user.get())) {
			return deleteOrDisableUser(adminID, user.get(), AppConstants.USER_ACCOUNT_DELETED,
					AppConstants.USER_ACCOUNT_DELETE_SUCCSESS);
		} else {
			throw new UserNotFoundException(AppConstants.ERROR_MESSAGE_USER_ID_NOT_FOUND);
		}
	}

	public ApplicationResponse disableUserByUserName(Long adminID, String username)
			throws IllegalArgumentException, UserNotFoundException, IOException, TemplateException, MessagingException {
		// TODO Auto-generated method stub
		User user = queryUserName(username);
		if (Objects.nonNull(user)) {
			return deleteOrDisableUser(adminID, user, AppConstants.USER_ACCOUNT_DISABLED,
					AppConstants.USER_ACCOUNT_DEACTIVE_SUCCSESS);
		} else {
			throw new UserNotFoundException(AppConstants.ERROR_MESSAGE_USER_ID_NOT_FOUND);
		}
	}

	public ApplicationResponse disableUserByUserId(Long adminID, Long userid)
			throws IllegalArgumentException, UserNotFoundException, IOException, TemplateException, MessagingException {
		// TODO Auto-generated method stub
		Optional<User> user = queryUserId(userid);
		if (Objects.nonNull(user) && Objects.nonNull(user.get())) {
			return deleteOrDisableUser(adminID, user.get(), AppConstants.USER_ACCOUNT_DISABLED,
					AppConstants.USER_ACCOUNT_DEACTIVE_SUCCSESS);
		} else {
			throw new UserNotFoundException(AppConstants.ERROR_MESSAGE_USER_ID_NOT_FOUND);
		}
	}

	private ApplicationResponse activateUser(Long adminID, User user, String subject, String action)
			throws IOException, TemplateException, MessagingException {
		user.setUserStatus(this.userUtility.getUserStatus(AppConstants.USER_ACCOUNT_ACTIVE));
		user.setModifiedBy(Objects.nonNull(adminID) ? adminID : null);
		user.setModifiedDateTime(LocalDateTime.now().atOffset(ZoneOffset.UTC).toLocalDateTime());
		user.setInvalidRetryCount(0);
		user.setPasswrd(this.userUtility.createDefaultPassword(user));
		user.setPasswrdExpiryDateTime(LocalDateTime.now().atOffset(ZoneOffset.UTC)
				.plusHours(AppConstants.ADD_PASSWORD_EXPIRY_2_HOURS).toLocalDateTime());
		user.setIsFirstTimeLogin(true);

		// send email
		this.userUtility.sendEmail(user, subject, action);

		this.userRepository.save(user);
		return this.userUtility.successApplicationResponse(AppConstants.USER_ACCOUNT_ACTIVE_SUCCSESS);
	}

	private ApplicationResponse deleteOrDisableUser(Long adminID, User user, String status, String message)
			throws IOException, TemplateException, MessagingException {
		user.setUserStatus(this.userUtility.getUserStatus(Objects.nonNull(status) ? status : null));
		user.setModifiedBy(Objects.nonNull(adminID) ? adminID : null);
		user.setModifiedDateTime(LocalDateTime.now().atOffset(ZoneOffset.UTC).toLocalDateTime());

		this.userRepository.save(user);
		return this.userUtility.successApplicationResponse(Objects.nonNull(message) ? message : null);
	}

	public UserInformation findUserById(Long id) throws IllegalArgumentException, UserNotFoundException {
		Optional<User> user = queryUserId(id);
		if (Objects.nonNull(user) && Objects.nonNull(user.get())) {
			User userDetails = user.get();
			return this.userToUserInformationDtoMapper.convert(userDetails); // dao to dto mapping
		} else {
			throw new UserNotFoundException(AppConstants.ERROR_MESSAGE_USER_ID_NOT_FOUND);
		}
	}

	public UserInformation findUserByUserName(String username) throws IllegalArgumentException, UserNotFoundException {
		User user = queryUserName(username);
		if (Objects.nonNull(user)) {
			return this.userToUserInformationDtoMapper.convert(user); // dao to dto mapping
		} else {
			throw new UserNotFoundException(AppConstants.ERROR_MESSAGE_USER_ACCOUNT_NOT_FOUND);
		}
	}

}
