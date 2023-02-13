package com.saemoim.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.dto.request.ReviewRequestDto;
import com.saemoim.dto.response.ReviewResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
	@Transactional
	@Override
	public ReviewResponseDto createReview(Long groupId, ReviewRequestDto requestDto, String username) {
		return null;
	}

	@Transactional
	@Override
	public ReviewResponseDto updateReview(Long reviewId, ReviewRequestDto requestDto, String username) {
		return null;
	}

	@Transactional
	@Override
	public void deleteReview(Long reviewId, String username) {

	}
}
