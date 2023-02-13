package com.saemoim.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.saemoim.dto.request.PostRequestDto;
import com.saemoim.dto.response.MessageResponseDto;
import com.saemoim.dto.response.PostResponseDto;
import com.saemoim.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PostController {

	// 전체 게시글 조회
	@GetMapping("/post")
	public List<PostResponseDto> getAllPosts() {
		return null;
	}

	// 선택한 게시글 조회
	@GetMapping("/posts/{postId}")
	public PostResponseDto getPost(@PathVariable Long postId) {
		return null;
	}

	// 게시글 생성
	@PostMapping("/groups/{groupId}/post")
	public PostResponseDto createPost(@PathVariable Long groupId, @Validated @RequestBody PostRequestDto requestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return null;
	}

	// 게시글 수정
	@PutMapping("/posts/{postId}")
	public PostResponseDto updatePost(@PathVariable Long postId,
		@Validated @RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		return null;
	}

	// 게시글 삭제
	@DeleteMapping("/posts/{postId}")
	public ResponseEntity<MessageResponseDto> deletePost(@PathVariable Long postId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return null;
	}
}
