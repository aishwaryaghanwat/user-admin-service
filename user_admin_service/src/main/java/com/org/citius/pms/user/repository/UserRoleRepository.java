package com.org.citius.pms.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.org.citius.pms.user.service.dao.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

	UserRole findByRoleName(String roleName);
}
