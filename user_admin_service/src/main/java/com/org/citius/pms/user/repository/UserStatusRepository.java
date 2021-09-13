package com.org.citius.pms.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.org.citius.pms.user.service.dao.UserStatus;

@Repository
public interface UserStatusRepository extends JpaRepository<UserStatus, Long> {

	UserStatus findByUserStatusCode(String userStatusCode);

}