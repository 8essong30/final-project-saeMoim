package com.saemoim.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saemoim.dto.response.StatusResponseDto;
import com.saemoim.dto.response.UserResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

	// 전체 회원 조회
	@GetMapping("/user")
	public List<UserResponseDto> getAllUsers() {
		return null;
	}

	// 관리자 게시글 삭제
	@DeleteMapping("/posts/{postId}")
	public StatusResponseDto deletePostByAdmin(@PathVariable Long postId) {
		return null;
	}

	//관리자 후기 삭제
	@DeleteMapping("/review/{reviewId}")
	public StatusResponseDto deleteReviewByAdmin(@PathVariable Long reviewId) {
		return null;
	}
}
