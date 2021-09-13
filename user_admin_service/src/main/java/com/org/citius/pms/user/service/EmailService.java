package com.org.citius.pms.user.service;

import java.io.IOException;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.org.citius.pms.user.util.constants.AppConstants;

import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
public class EmailService {

	// private static final String NOREPLY_ADDRESS = "admin@pms.com";

	@Autowired
	private JavaMailSender emailSender;

	@Autowired
	private FreeMarkerConfigurer freemarkerConfigurer;

	public void sendMessageUsingFreemarkerTemplate(String to, String subject, Map<String, Object> templateModel,
			String template) throws IOException, TemplateException, MessagingException {
		Template freemarkerTemplate = this.freemarkerConfigurer.getConfiguration().getTemplate(template);
		String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, templateModel);
		sendHtmlMessage(to, subject, htmlBody);
	}

	private void sendHtmlMessage(String to, String subject, String htmlBody) throws MessagingException {
		MimeMessage message = this.emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, AppConstants.UTF_8_ENCODING);
		// helper.setFrom(NOREPLY_ADDRESS);
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(htmlBody, true);
		emailSender.send(message);
	}

}
