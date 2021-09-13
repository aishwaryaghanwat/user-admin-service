package com.org.citius.pms.user.config.bean;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomInitializingBean implements InitializingBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomInitializingBean.class);

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@Bean
	public ModelMapper modelMapper() {
		LOGGER.info("Initializing ModelMapper started...");
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		LOGGER.info("Initializing ModelMapper completed...");
		return modelMapper;
	}

}