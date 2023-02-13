package com.saemoim.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saemoim.dto.response.BlackListResponseDto;
import com.saemoim.dto.response.MessageResponseDto;
import com.saemoim.service.BlackListServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BlackListController {
	private final BlackListServiceImpl blackListService;

	// 블랙리스트 목록 조회
	@GetMapping("/blacklist")
	public List<BlackListResponseDto> getBlacklists() {
		return blackListService.getBlacklists();
	}

	// 블랙리스트 등록
	@PostMapping("/blacklist/users/{userId}")
	public ResponseEntity<MessageResponseDto> addBlacklist(@PathVariable Long userId) {
		blackListService.addBlacklist(userId);
		return new ResponseEntity<>(new MessageResponseDto("블랙리스트 등록 완료"), HttpStatus.OK);
	}

	// 영구 블랙리스트 등록
	@PatchMapping("/blacklist/{blacklistId}")
	public ResponseEntity<MessageResponseDto> imposePermanentBan(@PathVariable Long blacklistId) {
		blackListService.imposePermanentBan(blacklistId);
		return new ResponseEntity<>(new MessageResponseDto("영구 블랙리스트 등록 완료"), HttpStatus.OK);
	}

	// 블랙리스트 해제
	@DeleteMapping("/blacklist/{blacklistId}")
	public ResponseEntity<MessageResponseDto> deleteBlacklist(@PathVariable Long blacklistId) {
		blackListService.deleteBlacklist(blacklistId);
		return new ResponseEntity<>(new MessageResponseDto("블랙리스트 해제 완료"), HttpStatus.OK);
	}
}
