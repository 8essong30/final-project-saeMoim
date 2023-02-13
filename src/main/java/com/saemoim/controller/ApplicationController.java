package com.saemoim.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saemoim.dto.response.ApplicationResponseDto;
import com.saemoim.dto.response.MessageResponseDto;
import com.saemoim.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ApplicationController {

	// 참가자가 신청한 모임내역 조회
	@GetMapping("/application")
	public List<ApplicationResponseDto> getMyApplications(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return null;
	}

	// 참가자가 모임 신청
	@PostMapping("/groups/{groupId}/application")
	public ResponseEntity<MessageResponseDto> applyGroup(@PathVariable Long groupId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return null;
	}

	// 참가자가 모임 신청 취소
	@DeleteMapping("application/{applicationId}")
	public ResponseEntity<MessageResponseDto> cancelApplication(@PathVariable Long applicationId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return null;
	}

	// 리더가 신청받은 모임내역 조회
	@GetMapping("/groups/{groupId}/application")
	public List<ApplicationResponseDto> getApplications(@PathVariable Long groupId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return null;
	}

	// 리더가 모임 신청 승인
	@PutMapping("/applications/{applicationId}/permit")
	public ResponseEntity<MessageResponseDto> permitApplication(@PathVariable Long applicationId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return null;
	}

	// 리더가 모임 신청 거절
	@PutMapping("/applications/{applicationId}/reject")
	public ResponseEntity<MessageResponseDto> rejectApplication(@PathVariable Long applicationId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return null;
	}

}
