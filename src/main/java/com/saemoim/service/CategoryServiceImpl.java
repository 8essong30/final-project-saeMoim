package com.saemoim.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.domain.Category;
import com.saemoim.dto.request.CategoryRequestDto;
import com.saemoim.dto.response.CategoryResponseDto;
import com.saemoim.exception.ErrorCode;
import com.saemoim.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
	private final CategoryRepository categoryRepository;

	@Transactional(readOnly = true)
	@Override
	public List<CategoryResponseDto> getCategories() {
		return categoryRepository.findByParentIdIsNull()
			.stream()
			.map(c -> c.toCategoryResponseDto(categoryRepository.findByParentIdIsNotNull()))
			.toList();
	}

	@Transactional
	@Override
	public void createCategory(CategoryRequestDto requestDto) {
		if (categoryRepository.existsByName(requestDto.getName())) {
			throw new IllegalArgumentException(ErrorCode.DUPLICATED_CATEGORY.getMessage());
		}

		Category category = Category.builder()
			.name(requestDto.getName())
			.build();

		categoryRepository.save(category);
	}

	@Transactional
	@Override
	public void createChildCategory(Long parentId, CategoryRequestDto requestDto) {
		if (categoryRepository.existsByName(requestDto.getName())) {
			throw new IllegalArgumentException(ErrorCode.DUPLICATED_CATEGORY.getMessage());
		}

		//아래 로직은 프론트에서 처리해주기 때문에 필요 없긴함
		Category findCategory = categoryRepository.findById(parentId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_EXIST_CATEGORY.getMessage())
		);
		if (findCategory.getParentId() != null) {
			throw new IllegalArgumentException(ErrorCode.NOT_PARENT_CATEGORY.getMessage());
		}

		Category category = Category.builder()
			.name(requestDto.getName())
			.parentId(parentId)
			.build();

		categoryRepository.save(category);
	}

	@Transactional
	@Override
	public void updateCategory(Long categoryId, CategoryRequestDto requestDto) {
		if (categoryRepository.existsByName(requestDto.getName())) {
			throw new IllegalArgumentException(ErrorCode.DUPLICATED_CATEGORY.getMessage());
		}

		Category category = categoryRepository.findById(categoryId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_EXIST_CATEGORY.getMessage())
		);

		category.updateCategory(requestDto.getName());
		categoryRepository.save(category);
	}

	@Transactional
	@Override
	public void deleteCategory(Long categoryId) {
		if (categoryRepository.existsByParentId(categoryId)) {
			throw new IllegalArgumentException(ErrorCode.NOT_EMPTY_CATEGORY.getMessage());
		}

		Category category = categoryRepository.findById(categoryId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_EXIST_CATEGORY.getMessage())
		);

		categoryRepository.delete(category);
	}
}
