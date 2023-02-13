package com.saemoim.service;

import java.util.List;

import com.saemoim.dto.response.UserResponseDto;

public interface AdminService {

	List<UserResponseDto> getAllUsers();

	void deletePostByAdmin(Long postId);

	void deleteReviewByAdmin(Long reviewId);

}
