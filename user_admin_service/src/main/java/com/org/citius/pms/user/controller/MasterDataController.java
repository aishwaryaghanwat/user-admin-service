package com.org.citius.pms.user.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.org.citius.pms.user.controller.dto.CountryDetails;
import com.org.citius.pms.user.controller.dto.TitleDetails;
import com.org.citius.pms.user.controller.dto.UserRoleDetails;
import com.org.citius.pms.user.service.CountryService;
import com.org.citius.pms.user.service.TitleService;
import com.org.citius.pms.user.service.UserRoleService;
import com.org.citius.pms.user.util.exception.CountryNotFoundException;
import com.org.citius.pms.user.util.exception.TitleNotFoundException;
import com.org.citius.pms.user.util.exception.UserRoleNotFoundException;

@RestController
@RequestMapping(path = "/masterlist")
@CrossOrigin("*")
public class MasterDataController {

	private static final Logger LOGGER = LoggerFactory.getLogger(MasterDataController.class);

	@Autowired
	private CountryService countryService;

	@Autowired
	private TitleService titleService;

	@Autowired
	private UserRoleService userRoleService;

	@GetMapping(path = "/country")
	@ResponseBody
	public Iterable<CountryDetails> getAllCountries() throws CountryNotFoundException {
		LOGGER.info("MasterDataController :: getAllCountries execution started");
		try {
			return this.countryService.getAllCountries();
		} finally {
			LOGGER.info("MasterDataController ::  getAllCountries execution completed");
		}
	}

	@GetMapping(path = "/title")
	@ResponseBody
	public Iterable<TitleDetails> getAllTitle() throws TitleNotFoundException {
		LOGGER.info("MasterDataController ::  getAllTitle execution started");
		try {
			return this.titleService.getAllTitles();
		} finally {
			LOGGER.info("MasterDataController :: getAllTitle execution completed");
		}
	}

	@GetMapping(path = "/role")
	@ResponseBody
	public Iterable<UserRoleDetails> getAllUserRole() throws UserRoleNotFoundException {
		LOGGER.info("MasterDataController ::  getAllUserRole  execution started");
		try {
			return this.userRoleService.getAllRoles();
		} finally {
			LOGGER.info("MasterDataController ::  getAllUserRole execution completed");
		}
	}

}
