package com.org.citius.pms.user.service.dao;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author AishwaryaG
 *
 */
@Entity
@Table(name = "user")
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false, length = 20)
	private Long id;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "title_id")
	private Title title;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "country_id")
	private Country country;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "user_status_id")
	private UserStatus userStatus;

	@Column(name = "first_name", nullable = false, length = 45)
	private String firstName;

	@Column(name = "last_name", nullable = false, length = 45)
	private String lastName;

	@Column(name = "gender", nullable = false, length = 25)
	private String gender;

	@Column(name = "email_id", unique = true, nullable = false, length = 50)
	private String emailId;

	@Column(name = "dob", nullable = false, columnDefinition = "DATE")
	private LocalDate dob;

	@Column(name = "contact_number", unique = true, nullable = false, columnDefinition = "BIGINT(10)")
	private Long contactNumber;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "user_role_id")
	private UserRole userRole;

	@Column(name = "invalid_retry_count", columnDefinition = "TINYINT(3)")
	private Integer invalidRetryCount;

	@Column(name = "is_first_time_login", nullable = false)
	private Boolean isFirstTimeLogin;

	@Column(name = "employee_id", unique = true, updatable = false, length = 20)
	private String employeeID;

	@Column(name = "passwrd", nullable = false, columnDefinition = "CHAR(128)")
	private String passwrd;

	@Column(name = "passwrd_expiry_date_time", columnDefinition = "TIMESTAMP")
	private LocalDateTime passwrdExpiryDateTime;

	@Column(name = "created_date_time", columnDefinition = "TIMESTAMP", updatable = false)
	private LocalDateTime createdDateTime;

	@Column(name = "created_by_id", updatable = false, length = 20)
	private Long createdBy;

	@Column(name = "modified_date_time", insertable = false, columnDefinition = "TIMESTAMP")
	private LocalDateTime modifiedDateTime;

	@Column(name = "modified_by_id", insertable = false, length = 20)
	private Long modifiedBy;

	public LocalDateTime getPasswrdExpiryDateTime() {
		return passwrdExpiryDateTime;
	}

	public void setPasswrdExpiryDateTime(LocalDateTime passwrdExpiryDateTime) {
		this.passwrdExpiryDateTime = passwrdExpiryDateTime;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Title getTitle() {
		return title;
	}

	public void setTitle(Title title) {
		this.title = title;
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

	public Long getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(Long contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(String employeeID) {
		this.employeeID = employeeID;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
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

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Long modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public UserStatus getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
	}

	public Integer getInvalidRetryCount() {
		return invalidRetryCount;
	}

	public void setInvalidRetryCount(Integer invalidRetryCount) {
		this.invalidRetryCount = invalidRetryCount;
	}

	public String getPasswrd() {
		return passwrd;
	}

	public void setPasswrd(String passwrd) {
		this.passwrd = passwrd;
	}

	public Boolean getIsFirstTimeLogin() {
		return isFirstTimeLogin;
	}

	public void setIsFirstTimeLogin(Boolean isFirstTimeLogin) {
		this.isFirstTimeLogin = isFirstTimeLogin;
	}

}
