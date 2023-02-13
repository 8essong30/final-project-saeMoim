package com.saemoim.dto.request;

import jakarta.validation.constraints.Email;
import lombok.Getter;

@Getter
public class EmailVerificationDto {
	@Email(message = "올바른 이메일 형식이 아닙니다.")
	private String email;
}
