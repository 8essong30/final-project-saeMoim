package com.saemoim.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.saemoim.dto.request.ReportRequestDto;
import com.saemoim.dto.response.MessageResponseDto;
import com.saemoim.dto.response.ReportResponseDto;
import com.saemoim.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ReportController {

	// 회원 신고 조회
	@GetMapping("/admin/report")
	public List<ReportResponseDto> getReportedUsers() {
		return null;
	}

	// 회원 신고
	@PostMapping("/report/users/{subjectUserId}")
	public ResponseEntity<MessageResponseDto> reportUser(@PathVariable Long subjectUserId,
		@Validated @RequestBody ReportRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		return null;
	}
}
