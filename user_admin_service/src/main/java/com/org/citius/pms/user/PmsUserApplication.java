package com.org.citius.pms.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.org.citius.pms")
public class PmsUserApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(PmsUserApplication.class);

	public static void main(String[] args) {

		LOGGER.info("PmsUserApplication starting...");
		SpringApplication.run(PmsUserApplication.class, args);
		LOGGER.info("PmsUserApplication started...");
	}

}
