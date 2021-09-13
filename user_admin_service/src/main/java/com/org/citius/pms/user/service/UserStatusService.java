package com.org.citius.pms.user.service;

import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.org.citius.pms.user.repository.UserStatusRepository;
import com.org.citius.pms.user.service.dao.UserStatus;
import com.org.citius.pms.user.util.constants.AppConstants;
import com.org.citius.pms.user.util.exception.UserStatusNotFoundException;

@Service
public class UserStatusService {

	@Autowired
	private UserStatusRepository userStatusRepository;

	@Transactional(readOnly = true)
	public Optional<UserStatus> queryById(Long id) throws IllegalArgumentException, UserStatusNotFoundException {
		if (Objects.isNull(id))
			throw new IllegalArgumentException("User Status ID cannot be null / empty...");
		return Optional.ofNullable(this.userStatusRepository.findById(id))
				.orElseThrow(() -> new UserStatusNotFoundException(AppConstants.ERROR_MESSAGE_USER_STATUS_NOT_FOUND));
	}

	@Transactional(readOnly = true)
	public UserStatus queryByStatusCodeName(String userStatusCode)
			throws IllegalArgumentException, UserStatusNotFoundException {
		if (Objects.isNull(userStatusCode))
			throw new IllegalArgumentException("User Status Code name cannot be null/ empty...");
		return Optional.ofNullable(this.userStatusRepository.findByUserStatusCode(userStatusCode))
				.orElseThrow(() -> new UserStatusNotFoundException(AppConstants.ERROR_MESSAGE_USER_STATUS_NOT_FOUND));
	}
}
