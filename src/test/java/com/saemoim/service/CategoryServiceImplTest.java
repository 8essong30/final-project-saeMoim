package com.saemoim.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.saemoim.domain.Category;
import com.saemoim.dto.request.CategoryRequestDto;
import com.saemoim.dto.response.CategoryResponseDto;
import com.saemoim.exception.ErrorCode;
import com.saemoim.repository.CategoryRepository;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

	@Mock
	CategoryRepository categoryRepository;

	@InjectMocks
	CategoryServiceImpl categoryService;

	@Test
	@DisplayName("카테고리 조회")
	void getCategories() {
		//given
		Category category = Category.builder()
			.name("여행")
			.build();
		Category childCategory = Category.builder()
			.parentId(1L)
			.name("부산")
			.build();
		List<Category> list = new ArrayList<>();
		list.add(category);
		List<Category> childList = new ArrayList<>();
		childList.add(childCategory);

		when(categoryRepository.findByParentIdIsNull()).thenReturn(list);
		when(categoryRepository.findByParentIdIsNotNull()).thenReturn(childList);

		//when
		List<CategoryResponseDto> responseDtoList = categoryService.getCategories();

		//then
		assertThat(responseDtoList.get(0).getName()).isEqualTo("여행");
	}

	@Test
	@DisplayName("부모카테고리 생성 성공")
	void createCategory() {
		//given
		CategoryRequestDto requestDto = CategoryRequestDto.builder()
			.name("여행")
			.build();

		Category category = Category.builder()
			.name(requestDto.getName())
			.build();

		when(categoryRepository.existsByName(anyString())).thenReturn(false);
		when(categoryRepository.save(any(Category.class))).thenReturn(category);
		//when
		categoryService.createCategory(requestDto);
		//then
		verify(categoryRepository).save(any(Category.class));
	}

	@Test
	@DisplayName("부모카테고리 생성 실패")
	void createCategoryFail() {
		//given
		CategoryRequestDto requestDto = CategoryRequestDto.builder()
			.name("여행")
			.build();

		when(categoryRepository.existsByName(anyString())).thenReturn(true);
		//when
		Exception exception = assertThrows(RuntimeException.class, () -> categoryService.createCategory(requestDto));
		//then
		assertThat(exception.getMessage()).isEqualTo(ErrorCode.DUPLICATED_CATEGORY.getMessage());
	}

	@Test
	@DisplayName("자식 카테고리 생성")
	void createChildCategory() {
		//given
		Long parentId = 1L;

		CategoryRequestDto requestDto = CategoryRequestDto.builder()
			.name("여행")
			.build();

		Category category = Category.builder()
			.name("맛집")
			.build();

		Category category2 = Category.builder()
			.parentId(1L)
			.name("맛집")
			.build();

		when(categoryRepository.existsByName(anyString())).thenReturn(false);
		when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
		when(categoryRepository.save(any(Category.class))).thenReturn(category2);
		//when
		categoryService.createChildCategory(parentId, requestDto);
		//then
		verify(categoryRepository).save(any(Category.class));
	}

	@Test
	@DisplayName("카테고리 수정")
	void updateCategory() {
		//given
		Long parentId = 1L;

		CategoryRequestDto requestDto = CategoryRequestDto.builder()
			.name("맛집")
			.build();

		Category categoryMock = mock(Category.class);

		when(categoryRepository.existsByName(anyString())).thenReturn(false);
		when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(categoryMock));
		doNothing().when(categoryMock).updateCategory(anyString());
		//when
		categoryService.updateCategory(parentId, requestDto);
		//then

		verify(categoryMock).updateCategory(requestDto.getName());
	}

	@Test
	@DisplayName("카테고리 삭제")
	void deleteCategory() {
		//given
		Long categoryId = 1L;

		Category category = Category.builder()
			.name("맛집")
			.build();

		when(categoryRepository.existsByParentId(anyLong())).thenReturn(false);
		when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
		doNothing().when(categoryRepository).delete(any(Category.class));

		//when
		categoryService.deleteCategory(categoryId);
		//then
		verify(categoryRepository).delete(category);
	}
}