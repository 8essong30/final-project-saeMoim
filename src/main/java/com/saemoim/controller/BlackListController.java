package com.saemoim.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saemoim.dto.response.BlackListResponseDto;
import com.saemoim.dto.response.StatusResponseDto;
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
	public StatusResponseDto addBlacklist(@PathVariable Long userId) {
		return blackListService.addBlacklist(userId);
	}

	// 영구 블랙리스트 등록
	@PatchMapping("/blacklist/{blacklistId}")
	public StatusResponseDto imposePermanentBan(@PathVariable Long blacklistId) {
		return blackListService.imposePermanentBan(blacklistId);
	}

	// 블랙리스트 해제
	@DeleteMapping("/blacklist/{blacklistId}")
	public StatusResponseDto deleteBlacklist(@PathVariable Long blacklistId) {
		return blackListService.deleteBlacklist(blacklistId);
	}
}
