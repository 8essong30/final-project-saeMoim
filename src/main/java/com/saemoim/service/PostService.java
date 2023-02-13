package com.saemoim.service;

import java.util.List;

import com.saemoim.dto.request.PostRequestDto;
import com.saemoim.dto.response.PostResponseDto;

public interface PostService {
	List<PostResponseDto> getAllPosts();

	PostResponseDto getPost(Long postId);

	PostResponseDto createPost(Long groupId, PostRequestDto requestDto, String username);

	PostResponseDto updatePost(Long postId, PostRequestDto requestDto, String username);

	void deletePost(Long postId, String username);
}
