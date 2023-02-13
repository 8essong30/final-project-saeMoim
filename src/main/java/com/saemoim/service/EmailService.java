package com.saemoim.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;


public interface EmailService {

	public void createCode();

	public MimeMessage createEmailForm(String email) throws MessagingException;

	public String sendEmail(String toEmail) throws MessagingException;
}
