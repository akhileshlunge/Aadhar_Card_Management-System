package com.aadhar.app.service;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.aadhar.app.controller.LoginController;
import com.aadhar.app.utility.Constants;


@Service("EmailService")
public class EmailServiceImpl implements EmailService {
	
	private static Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private JavaMailSender emailSender;    
    
    @Autowired
    private SpringTemplateEngine thymeleafTemplateEngine;
    
    @Value("classpath:/mail-logo.png")
    private Resource resourceFile;

    public void sendSimpleMessage(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();           
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            emailSender.send(message);
        } catch (MailException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void sendMessageUsingThymeleafTemplate(
        String to, String subject, Map<String, Object> templateModel)
            throws MessagingException {
    	logger.info(Constants.EMAIL_SERVICE + " Thymeleaf Template called");   
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);
        logger.info(Constants.EMAIL_SERVICE + " Thymeleaf Template context is : " + thymeleafContext.toString());  
        String htmlBody = thymeleafTemplateEngine.process("template-thymeleaf.html", thymeleafContext);
        logger.info(Constants.EMAIL_SERVICE + " Email will sent to " + to +" and Subject is : " + subject);
        sendHtmlMessage(to, subject, htmlBody);
    }

    private void sendHtmlMessage(String to, String subject, String htmlBody) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");      
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        helper.addInline("attachment.png", resourceFile);
        logger.info(Constants.EMAIL_SERVICE + " Email will sent to " + to +" and Content is set");
        emailSender.send(message);
    }
   
}