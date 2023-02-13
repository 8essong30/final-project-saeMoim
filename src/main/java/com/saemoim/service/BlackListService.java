package com.saemoim.service;

import java.util.List;

import com.saemoim.dto.response.BlackListResponseDto;

public interface BlackListService {

	List<BlackListResponseDto> getBlacklists();

	void addBlacklist(Long userId);

	void imposePermanentBan(Long userId);

	void deleteBlacklist(Long userId);

	void scheduledBlacklist();
}
