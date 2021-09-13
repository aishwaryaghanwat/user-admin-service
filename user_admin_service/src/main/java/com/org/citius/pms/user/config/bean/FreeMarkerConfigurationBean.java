package com.org.citius.pms.user.config.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;

public class FreeMarkerConfigurationBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(FreeMarkerConfigurationBean.class);

	@Bean
	public FreeMarkerConfigurer freemarkerClassLoaderConfig() {
		LOGGER.info("Initializing freemarkerClassLoaderConfig started...");
		Configuration configuration = new Configuration(Configuration.VERSION_2_3_27);
		TemplateLoader templateLoader = new ClassTemplateLoader(this.getClass(), "/templates");
		configuration.setTemplateLoader(templateLoader);
		FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
		freeMarkerConfigurer.setConfiguration(configuration);
		LOGGER.info("Initializing freemarkerClassLoaderConfig completed...");
		return freeMarkerConfigurer;
	}
}
