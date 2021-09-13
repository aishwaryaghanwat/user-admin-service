package com.org.citius.pms.user.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.org.citius.pms.user.controller.dto.CountryDetails;
import com.org.citius.pms.user.repository.CountryRepository;
import com.org.citius.pms.user.service.dao.Country;
import com.org.citius.pms.user.util.constants.AppConstants;
import com.org.citius.pms.user.util.exception.CountryNotFoundException;
import com.org.citius.pms.user.util.mapper.CountryToCountryDetailsDtoMapper;

@Service
public class CountryService {

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private CountryToCountryDetailsDtoMapper countryToCountryDetailsDtoMapper;

	@Transactional(readOnly = true)
	public Optional<Country> queryById(Long id) throws IllegalArgumentException, CountryNotFoundException {
		if (Objects.isNull(id))
			throw new IllegalArgumentException("Country ID cannot be null / empty...");
		return Optional.ofNullable(this.countryRepository.findById(id))
				.orElseThrow(() -> new CountryNotFoundException(AppConstants.ERROR_MESSAGE_COUNTRY_NOT_FOUND));
	}

	@Transactional(readOnly = true)
	public Country queryByCountryCode(String countryCode) throws IllegalArgumentException, CountryNotFoundException {
		if (Objects.isNull(countryCode))
			throw new IllegalArgumentException("Country Code cannot be null/ empty...");
		return Optional.ofNullable(this.countryRepository.findByCountryCode(countryCode))
				.orElseThrow(() -> new CountryNotFoundException(AppConstants.ERROR_MESSAGE_COUNTRY_NOT_FOUND));
	}

	@Transactional(readOnly = true)
	public Iterable<CountryDetails> getAllCountries() throws CountryNotFoundException {
		Iterable<Country> list = Optional.ofNullable(this.countryRepository.findAllByOrderByCountryNameAsc())
				.orElseThrow(() -> new CountryNotFoundException(AppConstants.ERROR_MESSAGE_COUNTRY_NOT_FOUND));
		Iterable<CountryDetails> dtoList = StreamSupport.stream(list.spliterator(), false)
				.map(this.countryToCountryDetailsDtoMapper::convert).collect(Collectors.toList());
		Collections.sort((List<CountryDetails>) dtoList);
		return dtoList;
	}
}
