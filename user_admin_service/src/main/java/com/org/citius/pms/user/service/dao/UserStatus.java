package com.org.citius.pms.user.service.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_status")
public class UserStatus implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", updatable = false, nullable = false, length = 20)
	private Long id;

	@Column(name = "user_status_description", nullable = false, updatable = false, length = 50)
	private String userStatusDesc;

	@Column(name = "user_status_code", unique = true, nullable = false, updatable = false, length = 20)
	private String userStatusCode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserStatusDesc() {
		return userStatusDesc;
	}

	public void setUserStatusDesc(String userStatusDesc) {
		this.userStatusDesc = userStatusDesc;
	}

	public String getUserStatusCode() {
		return userStatusCode;
	}

	public void setUserStatusCode(String userStatusCode) {
		this.userStatusCode = userStatusCode;
	}

}