package com.saemoim.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.domain.User;
import com.saemoim.domain.enums.UserRoleEnum;
import com.saemoim.dto.request.ProfileRequestDto;
import com.saemoim.dto.request.SignInRequestDto;
import com.saemoim.dto.request.SignUpRequestDto;
import com.saemoim.dto.response.ProfileResponseDto;
import com.saemoim.dto.response.SignInResponseDto;
import com.saemoim.exception.ErrorCode;
import com.saemoim.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	@Override
	public void signUp(SignUpRequestDto requestDto) {
		String email = requestDto.getEmail();
		String password = passwordEncoder.encode(requestDto.getPassword());
		String username = requestDto.getUsername();    // 유일한 닉네임임.
		UserRoleEnum role = UserRoleEnum.USER;

		// 중복 가입 검증
		if (userRepository.existsByEmail(email)) {
			throw new IllegalArgumentException(ErrorCode.DUPLICATED_EMAIL.getMessage());
		}

		if (userRepository.existsByUsername(username)) {
			throw new IllegalArgumentException(ErrorCode.DUPLICATED_USERNAME.getMessage());
		}

		User user = new User(email, password, username, role);
		userRepository.save(user);
	}

	@Transactional
	@Override
	public SignInResponseDto signIn(SignInRequestDto requestDto) {
		User savedUser = userRepository.findByEmail(requestDto.getEmail())
			.orElseThrow(() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage()));

		if (!passwordEncoder.matches(requestDto.getPassword(), savedUser.getPassword())) {
			throw new IllegalAccessError(ErrorCode.INVALID_PASSWORD.getMessage());
		}

		String email = savedUser.getEmail();
		UserRoleEnum role = savedUser.getRole();
		return new SignInResponseDto(email, role);
	}

	@Transactional
	@Override
	public void logout(String username) {

	}

	@Transactional
	@Override
	public void withdraw(String username) {

	}

	@Transactional(readOnly = true)
	@Override
	public ProfileResponseDto getProfile(Long userId) {
		return null;
	}

	@Transactional
	@Override
	public ProfileResponseDto updateProfile(String username, ProfileRequestDto requestDto) {
		return null;
	}
}
