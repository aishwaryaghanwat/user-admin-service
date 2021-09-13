package com.org.citius.pms.user.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.org.citius.pms.user.service.dao.User;

@SuppressWarnings("rawtypes")
@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long>, JpaSpecificationExecutor, Serializable {

	User findByEmailId(String userName);

	public List<User> findAll();

	User findByEmployeeID(String userName);

	Boolean existsByEmailId(String username);

	Boolean existsByEmployeeID(String employeeID);

	Boolean existsByContactNumber(Long contactNumber);

	Iterable<User> findAllByOrderByIdAsc();

}