package com.saemoim.service;

import java.util.Random;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

	private final JavaMailSender emailSender;
	private String authkey;

	@Override
	public void createCode() {

		Random random = new Random();
		StringBuffer key = new StringBuffer();

		/*
			인증키를 생성함.
			규칙 - 글자 수 12개, 영문(대,소문자), 숫자 혼용
		 */
		for(int i=0; i<12; i++) {
			int index = random.nextInt(3);

			switch(index) {
				case 0:
					key.append((char)((int)random.nextInt(26) + 97));
					break;
				case 1:
					key.append((char)((int)random.nextInt(26) + 65));
					break;
				case 2:
					key.append(random.nextInt(9));
					break;

			}
		}
		authkey = key.toString();
	}

	@Override
	public MimeMessage createEmailForm(String email) throws MessagingException {

		createCode();

		String fromEmail = "dpevent@naver.com";
		String toEmail = email; // 받는 사람
		String title = "회원가입 인증 번호 입니다.";

		MimeMessage message = emailSender.createMimeMessage();	// 마임 메세지 -
		message.addRecipients(MimeMessage.RecipientType.TO, toEmail);	//
		message.setSubject(title);	// 메일 제목
		message.setFrom(fromEmail);	// 메일을 보내는 주체(이메일) 설정
		message.setText(authkey);	// 메일 내용 설정. "utf-8" , "html" 등으로 추가 설정 가능

		return message;
	}

	@Override
	public String sendEmail(String toEmail) throws MessagingException {

		MimeMessage emailForm = createEmailForm(toEmail);
		emailSender.send(emailForm);
		return authkey;
	}
}
