package com.saemoim.service;

import java.util.List;

import com.saemoim.dto.response.MyGroupResponseDto;

public interface WishService {
	List<MyGroupResponseDto> getWishGroups(String username);

	void wishGroup(Long groupId, String username);

	void deleteWishGroup(Long groupId, String username);

}
