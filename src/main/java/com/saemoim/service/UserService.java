package com.saemoim.service;

import com.saemoim.dto.request.ProfileRequestDto;
import com.saemoim.dto.request.SignInRequestDto;
import com.saemoim.dto.request.SignUpRequestDto;
import com.saemoim.dto.response.AuthenticatedUserDto;
import com.saemoim.dto.response.ProfileResponseDto;
import com.saemoim.dto.response.StatusResponseDto;

public interface UserService {
	StatusResponseDto signUp(SignUpRequestDto requestDto);

	AuthenticatedUserDto signIn(SignInRequestDto requestDto);

	StatusResponseDto logout(String username);

	StatusResponseDto withdraw(String username);

	ProfileResponseDto getProfile(Long userId);

	ProfileResponseDto updateProfile(String username, ProfileRequestDto requestDto);

}
