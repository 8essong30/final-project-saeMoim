package com.saemoim.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saemoim.dto.response.MessageResponseDto;
import com.saemoim.dto.response.MyGroupResponseDto;
import com.saemoim.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class WishController {

	// 모임 즐겨찾기 조회
	@GetMapping("/groups/wish")
	public List<MyGroupResponseDto> getWishGroups(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return null;
	}

	// 모임 즐겨찾기 추가
	@PostMapping("/groups/{groupId}/wish")
	public ResponseEntity<MessageResponseDto> wishGroup(@PathVariable Long groupId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return null;
	}

	// 모임 즐겨찾기 해제
	@DeleteMapping("/groups/{groupId}/wish")
	public ResponseEntity<MessageResponseDto> deleteWishGroup(@PathVariable Long groupId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return null;
	}
}
