package com.org.citius.pms.user.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.org.citius.pms.user.controller.dto.UserRoleDetails;
import com.org.citius.pms.user.repository.UserRoleRepository;
import com.org.citius.pms.user.service.dao.UserRole;
import com.org.citius.pms.user.util.constants.AppConstants;
import com.org.citius.pms.user.util.exception.UserRoleNotFoundException;
import com.org.citius.pms.user.util.mapper.UserRoleToUserRoleDetailsDtoMapper;

@Service
public class UserRoleService {

	@Autowired
	private UserRoleRepository userRoleRepository;

	@Autowired
	private UserRoleToUserRoleDetailsDtoMapper userRoleToUserRoleDetailsDtoMapper;

	@Transactional(readOnly = true)
	public Optional<UserRole> queryById(Long id) throws IllegalArgumentException, UserRoleNotFoundException {
		if (Objects.isNull(id))
			throw new IllegalArgumentException("User role id cannot be null / empty...");
		return Optional.ofNullable(this.userRoleRepository.findById(id))
				.orElseThrow(() -> new UserRoleNotFoundException(AppConstants.ERROR_MESSAGE_USER_ROLE_NOT_FOUND));
	}

	@Transactional(readOnly = true)
	public UserRole queryByRoleName(String roleName) throws IllegalArgumentException, UserRoleNotFoundException {
		if (Objects.isNull(roleName))
			throw new IllegalArgumentException("User role name cannot be null/ empty...");
		return Optional.ofNullable(this.userRoleRepository.findByRoleName(roleName))
				.orElseThrow(() -> new UserRoleNotFoundException(AppConstants.ERROR_MESSAGE_USER_ROLE_NOT_FOUND));
	}

	@Transactional(readOnly = true)
	public Iterable<UserRoleDetails> getAllRoles() throws UserRoleNotFoundException {
		List<UserRole> list = Optional.ofNullable(this.userRoleRepository.findAll())
				.orElseThrow(() -> new UserRoleNotFoundException(AppConstants.ERROR_MESSAGE_USER_ROLE_NOT_FOUND));
		Iterable<UserRoleDetails> dtoList = StreamSupport.stream(list.spliterator(), false)
				.map(this.userRoleToUserRoleDetailsDtoMapper::convert).collect(Collectors.toList());
		Collections.sort((List<UserRoleDetails>) dtoList);
		return dtoList;
	}
}
