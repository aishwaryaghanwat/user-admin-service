package com.org.citius.pms.user.util.mapper;

import java.util.Objects;

import javax.annotation.PostConstruct;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.org.citius.pms.user.controller.dto.TitleDetails;
import com.org.citius.pms.user.service.dao.Title;

@Component
public class TitleToTitleDetailsDtoMapper {

	private static final Logger logger = LoggerFactory.getLogger(TitleToTitleDetailsDtoMapper.class);

	@Autowired
	private ModelMapper modelMapper;

	@PostConstruct
	protected void onPostConstruct() {
		this.initializeTypeMaps();
	}

	private TypeMap<Title, TitleDetails> typeMapDTO;

	private void initializeTypeMaps() {
		logger.info("initializing mapper to map DAO Title to DTO TitleDetails...");

		this.typeMapDTO = this.modelMapper.createTypeMap(Title.class, TitleDetails.class)
				.addMapping(Title::getId, TitleDetails::setId)
				.addMapping(Title::getTitleName, TitleDetails::setTitleName);
	}

	public TitleDetails convert(Title dao) {
		if (Objects.isNull(dao))
			return null;
		return this.typeMapDTO.map(dao);
	}

}
