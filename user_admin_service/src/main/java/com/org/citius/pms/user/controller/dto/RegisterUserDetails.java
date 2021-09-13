package com.org.citius.pms.user.controller.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.org.citius.pms.user.util.constants.AppConstants;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterUserDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty(value = "title", required = true)
	@NotNull(message = "Title cannot be null/empty")
	@Pattern(regexp = "^(Mr.|Mrs.|Dr.|Miss.)", message = "Invalid title, supported titles are Mr. / Mrs. / Dr. / Miss.")
	private String title;

	@JsonProperty(value = "countryCode", required = true)
	@NotNull(message = "Country code cannot be null/empty")
	@Pattern(regexp = "^[0-9]+$", message = "Invalid country code, entry does not meet criteria.")
	private String countryCode;

	@JsonProperty(value = "firstName", required = true)
	@NotNull(message = "First name cannot be null/empty")
	@Pattern(regexp = "^[a-zA-Z]+$", message = "Invalid first name, entry does not meet criteria")
	@Size(max = 45, message = "First name should be less than 45")
	private String firstName;

	@JsonProperty(value = "lastName", required = true)
	@NotNull(message = "Last name cannot be null/empty")
	@Pattern(regexp = "^[a-zA-Z]+$", message = "Invalid last name, entry does not meet criteria")
	@Size(max = 45, message = "Last name should be less than 45")
	private String lastName;

	@JsonProperty(value = "gender", required = true)
	@NotNull(message = "Gender cannot be null/empty")
	@Pattern(regexp = "^(Male|Female|Other)", message = "Invalid gender, supported is Male / Female / Other")
	private String gender;

	@JsonProperty(value = "emailId", required = true)
	@NotNull(message = "Email Id cannot be null/empty")
	@Pattern(regexp = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$", message = "Email Id entry does not meet criteria")
	@Size(max = 50, message = "Email Id should be less than 50")
	private String emailId;

	@JsonProperty(value = "dob", required = true)
	@NotNull(message = "Date-of-Birth cannot be null/empty")
	@Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Invalid Date-of-Birth(dob), supported criteria is yyyy-MM-dd")
	@JsonFormat(pattern = AppConstants.LOCAL_DATE_FORMAT)
	private String dob;

	@JsonProperty(value = "contactNumber", required = true)
	@NotNull(message = "Contact number cannot be null/empty")
	@Pattern(regexp = "^[1-9]{1}[0-9]{9}$", message = "Invalid contact number, entry does not meet criteria")
	private String contactNumber;

	@JsonProperty(value = "userRole", required = true)
	@NotNull(message = "User role cannot be null/empty")
	@Pattern(regexp = "^(ADMIN|PHYSICIAN|NURSE|PATIENT)", message = "Invalid user role, supported role is ADMIN or PHYSICIAN or NURSE or PATIENT")
	private String userRole;

	@JsonProperty(value = "employeeID")
	@Pattern(regexp = "^[0-9a-zA-Z]+$", message = "Invalid employee ID, entry does not meet criteria")
	@Size(max = 20, message = "Employee ID should be less than 20")
	private String employeeID;

	@JsonProperty(value = "password")
	@Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,}$", message = "Password entry does not meet criteria. Must contain atlease one digit, one capital and one lowercase charcter. No special characters are allowed")
	private String passwrd;

	@JsonProperty(value = "confirmPassword")
	@Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,}$", message = "Confirm Password entry does not meet criteria")
	private String confirmPasswrd;

	@JsonProperty(value = "createdDateTime")
	@Pattern(regexp = "^(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2})Z$", message = "Invalid create date time(createdDateTime), supported criteria is yyyy-MM-ddTHH:mm:ssZ")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConstants.LOCAL_DATE_TIME_FORMAT)
	private String createdDateTime;

	@JsonProperty(value = "createdById")
	private Long createdBy;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(String createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(String employeeID) {
		this.employeeID = employeeID;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public String getPasswrd() {
		return passwrd;
	}

	public void setPasswrd(String passwrd) {
		this.passwrd = passwrd;
	}

	public String getConfirmPasswrd() {
		return confirmPasswrd;
	}

	public void setConfirmPasswrd(String confirmPasswrd) {
		this.confirmPasswrd = confirmPasswrd;
	}

}
