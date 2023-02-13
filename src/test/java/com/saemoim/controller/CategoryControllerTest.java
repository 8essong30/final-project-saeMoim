package com.saemoim.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.google.gson.Gson;
import com.saemoim.domain.Category;
import com.saemoim.dto.request.CategoryRequestDto;
import com.saemoim.dto.response.CategoryResponseDto;
import com.saemoim.dto.response.StatusResponseDto;
import com.saemoim.service.CategoryServiceImpl;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

	@Mock
	private CategoryServiceImpl categoryService;

	@InjectMocks
	private CategoryController categoryController;

	private MockMvc mockMvc;

	@BeforeEach
	void init() {
		mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
	}

	@Test
	@DisplayName("카테고리 조회")
	void getCategories() throws Exception {
		//given
		Category category = Category.builder()
			.name("여행")
			.build();
		Category childCategory = Category.builder()
			.parentId(1L)
			.name("부산")
			.build();
		List<Category> childList = new ArrayList<>();
		childList.add(childCategory);

		CategoryResponseDto responseDto = new CategoryResponseDto(category, childList);
		List<CategoryResponseDto> responseDtoList = new ArrayList<>();
		responseDtoList.add(responseDto);

		when(categoryService.getCategories()).thenReturn(responseDtoList);
		//when
		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/category"));

		//then
		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("$[0]['name']", responseDto.getName()).exists());
	}

	@Test
	@DisplayName("부모 카테고리 생성")
	void createCategory() throws Exception {
		//given
		CategoryRequestDto requestDto = CategoryRequestDto.builder()
			.name("여행")
			.build();
		StatusResponseDto responseDto = new StatusResponseDto(HttpStatus.OK, requestDto.getName() + " 카테고리 생성 완료");
		when(categoryService.createCategory(any(CategoryRequestDto.class))).thenReturn(responseDto);

		//when
		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders.post("/admin/category")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new Gson().toJson(requestDto)));

		//then

		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("message", responseDto.getMessage()).exists());
	}

	@Test
	@DisplayName("자식 카테고리 생성")
	void createChildCategory() throws Exception {
		//given
		Long parentId = 1L;

		CategoryRequestDto requestDto = CategoryRequestDto.builder()
			.name("여행")
			.build();
		StatusResponseDto responseDto = new StatusResponseDto(HttpStatus.OK, requestDto.getName() + " 카테고리 생성 완료");
		when(categoryService.createChildCategory(anyLong(), any(CategoryRequestDto.class))).thenReturn(responseDto);

		//when
		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders.post("/admin/categories/{parentId}", 1L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(new Gson().toJson(requestDto)));

		//then

		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("message", responseDto.getMessage()).exists());
	}

	@Test
	@DisplayName("카테고리 수정")
	void updateCategory() throws Exception {
		//given
		Long parentId = 1L;

		CategoryRequestDto requestDto = CategoryRequestDto.builder()
			.name("여행")
			.build();
		StatusResponseDto responseDto = new StatusResponseDto(HttpStatus.OK, requestDto.getName() + " 카테고리 수정 완료");
		when(categoryService.updateCategory(anyLong(), any(CategoryRequestDto.class))).thenReturn(responseDto);

		//when
		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders.put("/admin/categories/{parentId}", 1L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(new Gson().toJson(requestDto)));

		//then

		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("message", responseDto.getMessage()).exists());
	}

	@Test
	@DisplayName("카테고리 삭제")
	void deleteCategory() throws Exception {
		//given
		Long parentId = 1L;

		StatusResponseDto responseDto = new StatusResponseDto(HttpStatus.OK, " 카테고리 삭제 완료");
		when(categoryService.deleteCategory(anyLong())).thenReturn(responseDto);
		//when
		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders.delete("/admin/categories/{categoryId}", parentId));

		//then
		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("message", responseDto.getMessage()).exists());
	}
}