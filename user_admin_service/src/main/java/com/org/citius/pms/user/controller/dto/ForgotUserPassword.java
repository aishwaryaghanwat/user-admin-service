package com.org.citius.pms.user.controller.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ForgotUserPassword implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty(value = "username", required = true)
	@NotNull(message = "Username cannot be null/empty")
	@Pattern(regexp = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$", message = "Username entry does not meet criteria")
	@Size(max = 50, message = "Username should be less than 50")
	private String userName;

	@JsonProperty(value = "passwrdForgotType", required = true)
	@NotNull(message = "Password Forgot Type cannot be null/empty")
	@Pattern(regexp = "^(CHANGE|FORGOT)", message = "Invalid data, supported are CHANGE or FORGOT")
	private String passwrdForgotType;

	@JsonProperty(value = "oldPassword")
	@Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,}$", message = "Old password entry does not meet criteria")
	private String oldPasswrd;

	@JsonProperty(value = "newPassword")
	@Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,}$", message = "New password entry does not meet criteria")
	private String newPasswrd;

	@JsonProperty(value = "confirmPassword")
	@Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,}$", message = "Confirm password entry does not meet criteria")
	private String confirmPasswrd;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPasswrdForgotType() {
		return passwrdForgotType;
	}

	public void setPasswrdForgotType(String passwrdForgotType) {
		this.passwrdForgotType = passwrdForgotType;
	}

	public String getOldPasswrd() {
		return oldPasswrd;
	}

	public void setOldPasswrd(String oldPasswrd) {
		this.oldPasswrd = oldPasswrd;
	}

	public String getNewPasswrd() {
		return newPasswrd;
	}

	public void setNewPasswrd(String newPasswrd) {
		this.newPasswrd = newPasswrd;
	}

	public String getConfirmPasswrd() {
		return confirmPasswrd;
	}

	public void setConfirmPasswrd(String confirmPasswrd) {
		this.confirmPasswrd = confirmPasswrd;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
