package com.org.citius.pms.user.controller.dto;

public enum PasswordForgotType {

	CHANGE("CHANGE"), FORGOT("FORGOT");

	String value;

	PasswordForgotType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
