package com.org.citius.pms.user.controller.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.org.citius.pms.user.util.constants.AppConstants;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInformation implements Serializable, Comparable<UserInformation> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty(value = "id")
	private Long id;

	@JsonProperty(value = "title")
	private String title;

	@JsonProperty(value = "countryCode")
	private String countryCode;

	@JsonProperty(value = "userStatus")
	private String userStatus;

	@JsonProperty(value = "firstName")
	private String firstName;

	@JsonProperty(value = "lastName")
	private String lastName;

	@JsonProperty(value = "gender")
	private String gender;

	@JsonProperty(value = "emailId")
	private String emailId;

	@JsonProperty(value = "username")
	private String userName;

	@JsonProperty(value = "dob")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConstants.LOCAL_DATE_FORMAT)
	private LocalDate dob;

	@JsonProperty(value = "contactNumber")
	private Long contactNumber;

	@JsonProperty(value = "userRole")
	private String userRole;

	@JsonProperty(value = "invalidRetryCount")
	private Integer invalidRetryCount;

	@JsonProperty(value = "isFirstTimeLogin")
	private Boolean isFirstTimeLogin;

	@JsonProperty(value = "employeeID")
	private String employeeID;

	@JsonProperty(value = "createdDateTime")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConstants.LOCAL_DATE_TIME_FORMAT)
	private LocalDateTime createdDateTime;

	@JsonProperty(value = "createdById")
	private Long createdBy;

	@JsonProperty(value = "modifiedDateTime")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConstants.LOCAL_DATE_TIME_FORMAT)
	private LocalDateTime modifiedDateTime;

	@JsonProperty(value = "modifiedById")
	private Long modifiedBy;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(Long contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public Integer getInvalidRetryCount() {
		return invalidRetryCount;
	}

	public void setInvalidRetryCount(Integer invalidRetryCount) {
		this.invalidRetryCount = invalidRetryCount;
	}

	public Boolean getIsFirstTimeLogin() {
		return isFirstTimeLogin;
	}

	public void setIsFirstTimeLogin(Boolean isFirstTimeLogin) {
		this.isFirstTimeLogin = isFirstTimeLogin;
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

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public LocalDateTime getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(LocalDateTime createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public LocalDateTime getModifiedDateTime() {
		return modifiedDateTime;
	}

	public void setModifiedDateTime(LocalDateTime modifiedDateTime) {
		this.modifiedDateTime = modifiedDateTime;
	}

	public Long getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Long modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	@Override
	public int compareTo(UserInformation obj) {
		// TODO Auto-generated method stub
		return this.id.compareTo(obj.getId());
	}

}
