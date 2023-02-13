package com.saemoim.service;

import com.saemoim.dto.request.ReviewRequestDto;
import com.saemoim.dto.response.ReviewResponseDto;

public interface ReviewService {
	ReviewResponseDto createReview(Long groupId, ReviewRequestDto requestDto, String username);

	ReviewResponseDto updateReview(Long reviewId, ReviewRequestDto requestDto, String username);

	void deleteReview(Long reviewId, String username);
}
