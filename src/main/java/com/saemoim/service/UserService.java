package com.saemoim.service;

import com.saemoim.dto.request.ProfileRequestDto;
import com.saemoim.dto.request.SignInRequestDto;
import com.saemoim.dto.request.SignUpRequestDto;
import com.saemoim.dto.response.ProfileResponseDto;
import com.saemoim.dto.response.SignInResponseDto;

public interface UserService {
	void signUp(SignUpRequestDto requestDto);

	SignInResponseDto signIn(SignInRequestDto requestDto);

	void logout(String username);

	void withdraw(String username);

	ProfileResponseDto getProfile(Long userId);

	ProfileResponseDto updateProfile(String username, ProfileRequestDto requestDto);

}
