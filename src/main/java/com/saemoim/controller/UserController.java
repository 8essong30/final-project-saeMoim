package com.saemoim.controller;

import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.saemoim.dto.request.ProfileRequestDto;
import com.saemoim.dto.request.SignInRequestDto;
import com.saemoim.dto.request.SignUpRequestDto;
import com.saemoim.dto.response.MessageResponseDto;
import com.saemoim.dto.response.ProfileResponseDto;
import com.saemoim.dto.response.SignInResponseDto;
import com.saemoim.jwt.JwtUtil;
import com.saemoim.security.UserDetailsImpl;
import com.saemoim.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final JwtUtil jwtUtil;

	// 회원 가입
	@PostMapping("/sign-up")
	public ResponseEntity<MessageResponseDto> signUp(@Validated @RequestBody SignUpRequestDto requestDto) {
		userService.signUp(requestDto);
		return new ResponseEntity<>(new MessageResponseDto("회원가입 완료"), HttpStatus.OK);
	}

	// 로그인
	@PostMapping("/sign-in")
	public ResponseEntity<MessageResponseDto> signIn(@RequestBody SignInRequestDto requestDto) {
		// 로그인 성공 시
		// 토큰 발급

		SignInResponseDto signInResponseDto = userService.signIn(requestDto);
		// 토큰 발급 - 이름(이메일) 과 권한 필요
		String token = jwtUtil.createToken(signInResponseDto.getEmail(), signInResponseDto.getRole());
		// 토큰 header 에 넣기
		HttpHeaders headers = new HttpHeaders();
		headers.add(JwtUtil.AUTHORIZATION_HEADER, token);
		headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

		return new ResponseEntity<>(new MessageResponseDto("로그인 완료"), headers, HttpStatus.OK);
	}

	// @AuthenticationPrincipal
	// Authentication = 인증객체의 principal 부분의 값을 가져옴
	// principal 에는 userDetails 가 있고, 이것은 user, username, password 가 들어있다.)
	// 로그아웃
	@PostMapping("/logout")
	public ResponseEntity<MessageResponseDto> logout(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return null;
	}

	// 회원 탈퇴
	@DeleteMapping("/withdrawal")
	public ResponseEntity<MessageResponseDto> withdraw(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return null;
	}

	// 프로필 조회
	@GetMapping("/profile/users/{userId}")
	public ProfileResponseDto getProfile(@PathVariable Long userId) {
		return null;
	}

	// 프로필 수정
	@PatchMapping("/profile")
	public ProfileResponseDto updateProfile(@Validated @RequestBody ProfileRequestDto requestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return null;
	}
}
