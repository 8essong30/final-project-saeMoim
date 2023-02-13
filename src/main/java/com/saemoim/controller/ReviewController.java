package com.saemoim.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.saemoim.dto.request.ReviewRequestDto;
import com.saemoim.dto.response.MessageResponseDto;
import com.saemoim.dto.response.ReviewResponseDto;
import com.saemoim.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ReviewController {

	// 모임 후기 작성
	@PostMapping("/groups/{groupId}/review")
	public ReviewResponseDto createReview(@PathVariable Long groupId,
		@Validated @RequestBody ReviewRequestDto requestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return null;
	}

	// 모임 후기 수정
	@PutMapping("/reviews/{reviewId}")
	public ReviewResponseDto updateReview(@PathVariable Long reviewId,
		@Validated @RequestBody ReviewRequestDto requestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return null;
	}

	// 모임 후기 삭제
	@DeleteMapping("/reviews/{reviewId}")
	public ResponseEntity<MessageResponseDto> deleteReview(@PathVariable Long reviewId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return null;
	}
}
