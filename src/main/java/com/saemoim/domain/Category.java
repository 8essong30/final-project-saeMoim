package com.saemoim.domain;

import java.util.List;

import com.saemoim.dto.response.CategoryResponseDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Category extends TimeStamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String name;

	private Long parentId;

	@Builder
	public Category(String name, Long parentId) {
		this.name = name;
		this.parentId = parentId;
	}

	public CategoryResponseDto toCategoryResponseDto(List<Category> categories) {
		return new CategoryResponseDto(this, categories);
	}

	public void updateCategory(String categoryName) {
		this.name = categoryName;
	}
}
