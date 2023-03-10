package com.saemoim.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.saemoim.dto.request.PostRequestDto;
import com.saemoim.dto.response.GenericsResponseDto;
import com.saemoim.dto.response.PostResponseDto;
import com.saemoim.security.UserDetailsImpl;
import com.saemoim.service.PostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PostController {
	private final PostService postService;

	// 그룹 전체 게시글 조회
	@GetMapping("/groups/{groupId}/post")
	public ResponseEntity<Page<PostResponseDto>> getAllPostsByGroup(@PathVariable Long groupId,
		@PageableDefault(size = 15, page = 0) Pageable pageable,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return ResponseEntity.ok().body(postService.getAllPostsByGroup(groupId, pageable, userDetails.getId()));
	}

	// 선택한 게시글 조회
	@GetMapping("/posts/{postId}")
	public ResponseEntity<PostResponseDto> getPost(@PathVariable Long postId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return ResponseEntity.ok().body(postService.getPost(postId, userDetails.getId()));
	}

	// 게시글 생성
	@PostMapping(value = "/groups/{groupId}/post", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
		MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<GenericsResponseDto> createPost(@PathVariable Long groupId,
		@Validated @RequestPart("requestDto") PostRequestDto requestDto
		, @RequestPart(required = false, name = "img") MultipartFile multipartFile,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		postService.createPost(groupId, requestDto, userDetails.getId(), multipartFile);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(new GenericsResponseDto("게시글 생성이 완료 되었습니다."));
	}

	// 게시글 수정
	@PutMapping(value = "/posts/{postId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
		MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<GenericsResponseDto> updatePost(@PathVariable Long postId,
		@Validated @RequestPart PostRequestDto requestDto,
		@RequestPart(required = false, name = "img") MultipartFile multipartFile,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		postService.updatePost(postId, requestDto, userDetails.getId(), multipartFile);
		return ResponseEntity.ok().body(new GenericsResponseDto("게시글 수정이 완료 되었습니다."));
	}

	// 게시글 삭제
	@DeleteMapping("/posts/{postId}")
	public ResponseEntity<GenericsResponseDto> deletePost(@PathVariable Long postId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		postService.deletePost(postId, userDetails.getId());
		return ResponseEntity.ok().body(new GenericsResponseDto("게시글 삭제가 완료 되었습니다."));
	}

	// 관리자 게시글 삭제
	@DeleteMapping("/admin/posts/{postId}")
	public ResponseEntity<GenericsResponseDto> deletePostByAdmin(@PathVariable Long postId) {
		postService.deletePostByAdmin(postId);
		return ResponseEntity.ok().body(new GenericsResponseDto("게시글 삭제가 완료 되었습니다."));
	}
}
