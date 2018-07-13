package com.bridgelabz.fundoonotes.user.services;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.bridgelabz.fundoonotes.user.models.Email;

public class EmailServiceImpl implements EmailService {

	JavaMailSender mailSender;
	
	@Override
	public void sendEmail(Email email ) throws MessagingException {
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(message , true);
		
		messageHelper.setTo(email.getTo());
		messageHelper.setSubject(email.getSubject());
		messageHelper.setText(email.getText());
		
		mailSender.send(message);
		
	}

}
