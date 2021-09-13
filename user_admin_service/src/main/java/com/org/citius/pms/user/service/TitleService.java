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

import com.org.citius.pms.user.controller.dto.TitleDetails;
import com.org.citius.pms.user.repository.TitleRepository;
import com.org.citius.pms.user.service.dao.Title;
import com.org.citius.pms.user.util.constants.AppConstants;
import com.org.citius.pms.user.util.exception.TitleNotFoundException;
import com.org.citius.pms.user.util.mapper.TitleToTitleDetailsDtoMapper;

@Service
public class TitleService {

	@Autowired
	private TitleRepository titleRepository;

	@Autowired
	private TitleToTitleDetailsDtoMapper titleToTitleDetailsDtoMapper;

	@Transactional(readOnly = true)
	public Optional<Title> queryById(Long id) throws IllegalArgumentException, TitleNotFoundException {
		if (Objects.isNull(id))
			throw new IllegalArgumentException("Title cannot be null / empty...");
		return Optional.ofNullable(this.titleRepository.findById(id))
				.orElseThrow(() -> new TitleNotFoundException(AppConstants.ERROR_MESSAGE_TITLE_NOT_FOUND));
	}

	@Transactional(readOnly = true)
	public Title queryByTitleName(String titleName) throws IllegalArgumentException, TitleNotFoundException {
		if (Objects.isNull(titleName))
			throw new IllegalArgumentException("Title name cannot be null/ empty...");
		return Optional.ofNullable(this.titleRepository.findByTitleName(titleName))
				.orElseThrow(() -> new TitleNotFoundException(AppConstants.ERROR_MESSAGE_TITLE_NOT_FOUND));
	}

	@Transactional(readOnly = true)
	public Iterable<TitleDetails> getAllTitles() throws TitleNotFoundException {
		List<Title> list = Optional.ofNullable(this.titleRepository.findAll())
				.orElseThrow(() -> new TitleNotFoundException(AppConstants.ERROR_MESSAGE_TITLE_NOT_FOUND));
		Iterable<TitleDetails> dtoList = StreamSupport.stream(list.spliterator(), false)
				.map(this.titleToTitleDetailsDtoMapper::convert).collect(Collectors.toList());
		Collections.sort((List<TitleDetails>) dtoList);
		return dtoList;
	}
}
