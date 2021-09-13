package com.org.citius.pms.user.util.mapper;

import java.util.Objects;

import javax.annotation.PostConstruct;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.org.citius.pms.user.controller.dto.CountryDetails;
import com.org.citius.pms.user.service.dao.Country;

@Component
public class CountryToCountryDetailsDtoMapper {

	private static final Logger logger = LoggerFactory.getLogger(CountryToCountryDetailsDtoMapper.class);

	@Autowired
	private ModelMapper modelMapper;

	@PostConstruct
	protected void onPostConstruct() {
		this.initializeTypeMaps();
	}

	private TypeMap<Country, CountryDetails> typeMapDTO;

	private void initializeTypeMaps() {
		logger.info("initializing mapper to map DAO Country to DTO CountryDetails...");

		this.typeMapDTO = this.modelMapper.createTypeMap(Country.class, CountryDetails.class)
				.addMapping(Country::getId, CountryDetails::setId)
				.addMapping(Country::getCountryCode, CountryDetails::setCountryCode)
				.addMapping(Country::getCountryName, CountryDetails::setCountryName);
	}

	public CountryDetails convert(Country dao) {
		if (Objects.isNull(dao))
			return null;
		return this.typeMapDTO.map(dao);
	}

}
