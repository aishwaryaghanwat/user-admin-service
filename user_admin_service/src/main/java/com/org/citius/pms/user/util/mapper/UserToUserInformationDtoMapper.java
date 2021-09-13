package com.org.citius.pms.user.util.mapper;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.org.citius.pms.user.controller.dto.UserInformation;
import com.org.citius.pms.user.service.dao.Country;
import com.org.citius.pms.user.service.dao.Title;
import com.org.citius.pms.user.service.dao.User;
import com.org.citius.pms.user.service.dao.UserRole;
import com.org.citius.pms.user.service.dao.UserStatus;

@Component
public class UserToUserInformationDtoMapper {

	private static final Logger logger = LoggerFactory.getLogger(UserToUserInformationDtoMapper.class);

	@Autowired
	private ModelMapper modelMapper;

	@PostConstruct
	protected void onPostConstruct() {
		this.initializeTypeMaps();
	}

	private TypeMap<User, UserInformation> typeMapDTO;

	private void initializeTypeMaps() {
		logger.info("initializing mapper to map DAO User to DTO UserInformation...");

		Converter<Title, String> convertTitleToTitleName = ctx -> Objects.isNull(ctx.getSource()) ? null
				: ctx.getSource().getTitleName();

		Converter<Country, String> convertCountryToCountryCode = ctx -> Objects.isNull(ctx.getSource()) ? null
				: ctx.getSource().getCountryCode();

		Converter<UserStatus, String> convertUserStatusToUserStatusCode = ctx -> Objects.isNull(ctx.getSource()) ? null
				: ctx.getSource().getUserStatusCode();

		Converter<UserRole, String> convertUserRoleToUserRoleName = ctx -> Objects.isNull(ctx.getSource()) ? null
				: ctx.getSource().getRoleName();

		Converter<LocalDateTime, LocalDateTime> convertLocalDateTimeUsingZoneOffSetUTC = ctx -> Objects
				.isNull(ctx.getSource()) ? null : ctx.getSource().atOffset(ZoneOffset.UTC).toLocalDateTime();

		this.typeMapDTO = this.modelMapper.createTypeMap(User.class, UserInformation.class)
				.addMapping(User::getId, UserInformation::setId)
				.addMapping(User::getFirstName, UserInformation::setFirstName)
				.addMapping(User::getLastName, UserInformation::setLastName)
				.addMapping(User::getEmailId, UserInformation::setEmailId)
				.addMapping(User::getEmailId, UserInformation::setUserName)
				.addMapping(User::getContactNumber, UserInformation::setContactNumber)
				.addMapping(User::getInvalidRetryCount, UserInformation::setInvalidRetryCount)
				.addMapping(User::getIsFirstTimeLogin, UserInformation::setIsFirstTimeLogin)
				.addMapping(User::getEmployeeID, UserInformation::setEmployeeID)
				.addMapping(User::getCreatedBy, UserInformation::setCreatedBy)
				.addMapping(User::getModifiedBy, UserInformation::setModifiedBy)
				.addMapping(User::getGender, UserInformation::setGender)
				.addMapping(User::getDob, UserInformation::setDob)
				.addMappings(mapper -> mapper.using(convertLocalDateTimeUsingZoneOffSetUTC)
						.map(User::getCreatedDateTime, UserInformation::setCreatedDateTime))
				.addMappings(mapper -> mapper.using(convertLocalDateTimeUsingZoneOffSetUTC)
						.map(User::getModifiedDateTime, UserInformation::setModifiedDateTime))
				.addMappings(
						mapper -> mapper.using(convertTitleToTitleName).map(User::getTitle, UserInformation::setTitle))
				.addMappings(mapper -> mapper.using(convertCountryToCountryCode).map(User::getCountry,
						UserInformation::setCountryCode))
				.addMappings(mapper -> mapper.using(convertUserStatusToUserStatusCode).map(User::getUserStatus,
						UserInformation::setUserStatus))
				.addMappings(mapper -> mapper.using(convertUserRoleToUserRoleName).map(User::getUserRole,
						UserInformation::setUserRole));
	}

	public UserInformation convert(User dao) {
		if (Objects.isNull(dao))
			return null;
		return this.typeMapDTO.map(dao);
	}
}