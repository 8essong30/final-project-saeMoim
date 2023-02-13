package com.saemoim.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.saemoim.dto.request.CategoryRequestDto;
import com.saemoim.dto.response.CategoryResponseDto;
import com.saemoim.dto.response.MessageResponseDto;
import com.saemoim.service.CategoryServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CategoryController {

	private final CategoryServiceImpl categoryService;

	// 카테고리 조회
	@GetMapping("/category")
	public List<CategoryResponseDto> getCategories() {
		return categoryService.getCategories();
	}

	// 카테고리 생성
	@PostMapping("/admin/category")
	public ResponseEntity<MessageResponseDto> createCategory(@Validated @RequestBody CategoryRequestDto requestDto) {
		categoryService.createCategory(requestDto);
		return new ResponseEntity<>(new MessageResponseDto(requestDto.getName() + " 카테고리 생성 완료"), HttpStatus.OK);
	}

	// 자식 카테고리 생성
	@PostMapping("/admin/categories/{parentId}")
	public ResponseEntity<MessageResponseDto> createChildCategory(@PathVariable Long parentId,
		@Validated @RequestBody CategoryRequestDto requestDto) {
		categoryService.createChildCategory(parentId, requestDto);
		return new ResponseEntity<>(new MessageResponseDto(requestDto.getName() + " 카테고리 생성 완료"), HttpStatus.OK);
	}

	// 카테고리 수정
	@PutMapping("/admin/categories/{categoryId}")
	public ResponseEntity<MessageResponseDto> updateCategory(@PathVariable Long categoryId,
		@Validated @RequestBody CategoryRequestDto requestDto) {
		categoryService.updateCategory(categoryId, requestDto);
		return new ResponseEntity<>(new MessageResponseDto(requestDto.getName() + " 카테고리 수정 완료"), HttpStatus.OK);
	}

	// 카테고리 삭제
	@DeleteMapping("/admin/categories/{categoryId}")
	public ResponseEntity<MessageResponseDto> deleteCategory(@PathVariable Long categoryId) {
		categoryService.deleteCategory(categoryId);
		return new ResponseEntity<>(new MessageResponseDto("카테고리 삭제 완료"), HttpStatus.OK);
	}
}
