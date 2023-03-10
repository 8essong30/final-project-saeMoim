package com.saemoim.dto.response;

import com.saemoim.domain.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ProfileResponseDto {
	private Long id;
	private String username;
	private String content;
	private String imagePath;

	public ProfileResponseDto(User user) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.content = user.getContent();
		this.imagePath = user.getImagePath();
	}
}
