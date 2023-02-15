package com.saemoim.service;

import java.util.List;

import com.saemoim.dto.request.PostRequestDto;
import com.saemoim.dto.response.PostResponseDto;

public interface PostService {
	List<PostResponseDto> getAllPosts();

	PostResponseDto getPost(Long postId);

	PostResponseDto createPost(Long groupId, PostRequestDto requestDto, Long userId);

	PostResponseDto updatePost(Long postId, PostRequestDto requestDto, Long userId);

	void deletePost(Long postId, Long userId);

	void deletePostByAdmin(Long postId);
}
