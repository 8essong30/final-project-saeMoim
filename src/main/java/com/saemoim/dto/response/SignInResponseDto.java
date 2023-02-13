package com.saemoim.dto.response;

import com.saemoim.domain.enums.UserRoleEnum;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignInResponseDto {

	private String email;
	private UserRoleEnum role;

	public SignInResponseDto(String email, UserRoleEnum role) {
		this.email = email;
		this.role = role;
	}
}
