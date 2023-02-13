package com.saemoim.service;

import java.util.List;

import com.saemoim.dto.request.CategoryRequestDto;
import com.saemoim.dto.response.CategoryResponseDto;

public interface CategoryService {
	List<CategoryResponseDto> getCategories();

	void createCategory(CategoryRequestDto requestDto);

	void createChildCategory(Long parentId, CategoryRequestDto requestDto);

	void updateCategory(Long categoryId, CategoryRequestDto requestDto);

	void deleteCategory(Long categoryId);
}
