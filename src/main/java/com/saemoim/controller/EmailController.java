package com.saemoim.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.saemoim.dto.request.EmailVerificationDto;
import com.saemoim.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class EmailController {

	private final EmailService emailService;

	@PostMapping("/email")
	public String mailConfirm(@RequestBody @Valid EmailVerificationDto emailVerificationDto) throws MessagingException {
		emailService.sendEmail(emailVerificationDto.getEmail());
		return "1";
	}

}
