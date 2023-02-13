package com.saemoim.service;

import java.util.List;

import com.saemoim.dto.response.BlackListResponseDto;
import com.saemoim.dto.response.StatusResponseDto;

public interface BlackListService {

	List<BlackListResponseDto> getBlacklists();

	StatusResponseDto addBlacklist(Long userId);

	StatusResponseDto imposePermanentBan(Long userId);

	StatusResponseDto deleteBlacklist(Long userId);

	void scheduledBlacklist();
}
