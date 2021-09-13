package com.org.citius.pms.user.service.dao.specifications;

import java.util.Objects;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;

import com.org.citius.pms.user.service.dao.User;
import com.org.citius.pms.user.service.dao.UserRole;
import com.org.citius.pms.user.service.dao.UserStatus;

public class UserSpecs {

	public static Specification<User> filterById(Long filterId) {
		if (Objects.isNull(filterId))
			return emptyAnd();
		return (root, query, cb) -> cb.equal(root.get(User_.ID), filterId);
	}

	public static Specification<User> filterByFirstName(String firstName) {
		return Optional.ofNullable(firstName).map(pattern -> {
			String trimmedPattern = pattern.trim();
			if (trimmedPattern.isEmpty())
				return emptyAnd();
			return (Specification<User>) (root, query, cb) -> cb.like(cb.lower(root.get(User_.FIRST_NAME)),
					String.format("%%%s%%", trimmedPattern).toLowerCase());
		}).orElse(emptyAnd());
	}

	public static Specification<User> filterByEmailId(String emailId) {
		return Optional.ofNullable(emailId).map(pattern -> {
			String trimmedPattern = pattern.trim();
			if (trimmedPattern.isEmpty())
				return emptyAnd();
			return (Specification<User>) (root, query, cb) -> cb.like(cb.lower(root.get(User_.EMAIL_ID)),
					String.format("%%%s%%", trimmedPattern).toLowerCase());
		}).orElse(emptyAnd());
	}

	public static Specification<User> filterByLastName(String lastName) {
		return Optional.ofNullable(lastName).map(pattern -> {
			String trimmedPattern = pattern.trim();
			if (trimmedPattern.isEmpty())
				return emptyAnd();
			return (Specification<User>) (root, query, cb) -> cb.like(cb.lower(root.get(User_.LAST_NAME)),
					String.format("%%%s%%", trimmedPattern).toLowerCase());
		}).orElse(emptyAnd());
	}

	public static Specification<User> filterByUserStatus(UserStatus userStatus) {
		if (Objects.isNull(userStatus))
			return emptyAnd();
		return (root, query, cb) -> cb.equal(root.get(User_.USER_STATUS), userStatus);
	}

	public static Specification<User> filterByUserRole(UserRole userRole) {
		if (Objects.isNull(userRole))
			return emptyAnd();
		return (root, query, cb) -> cb.equal(root.get(User_.USER_ROLE), userRole);
	}

	private static Specification<User> emptyAnd() {
		return (root, query, cb) -> cb.and();
	}

}
