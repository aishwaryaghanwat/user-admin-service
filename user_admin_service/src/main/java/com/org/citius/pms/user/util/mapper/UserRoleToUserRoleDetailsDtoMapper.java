package com.org.citius.pms.user.util.mapper;

import java.util.Objects;

import javax.annotation.PostConstruct;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.org.citius.pms.user.controller.dto.UserRoleDetails;
import com.org.citius.pms.user.service.dao.UserRole;

@Component
public class UserRoleToUserRoleDetailsDtoMapper {

	private static final Logger logger = LoggerFactory.getLogger(UserRoleToUserRoleDetailsDtoMapper.class);

	@Autowired
	private ModelMapper modelMapper;

	@PostConstruct
	protected void onPostConstruct() {
		this.initializeTypeMaps();
	}

	private TypeMap<UserRole, UserRoleDetails> typeMapDTO;

	private void initializeTypeMaps() {
		logger.info("initializing mapper to map DAO UserRole to DTO UserRoleDetails...");

		this.typeMapDTO = this.modelMapper.createTypeMap(UserRole.class, UserRoleDetails.class)
				.addMapping(UserRole::getId, UserRoleDetails::setId)
				.addMapping(UserRole::getRoleType, UserRoleDetails::setRoleType)
				.addMapping(UserRole::getRoleName, UserRoleDetails::setRoleName);
	}

	public UserRoleDetails convert(UserRole dao) {
		if (Objects.isNull(dao))
			return null;
		return this.typeMapDTO.map(dao);
	}

}
