package com.saemoim.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.dto.request.CommentRequestDto;
import com.saemoim.dto.response.CommentResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
	@Transactional
	@Override
	public CommentResponseDto createComment(Long postId, CommentRequestDto requestDto, String username) {
		return null;
	}

	@Transactional
	@Override
	public CommentResponseDto updateComment(Long commentId, CommentRequestDto requestDto, String username) {
		return null;
	}

	@Transactional
	@Override
	public void deleteComment(Long commentId, String username) {

	}
}
