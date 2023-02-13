package com.saemoim.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.domain.Category;

@SpringBootTest
@Transactional
class CategoryRepositoryTest {
	@Autowired
	CategoryRepository categoryRepository;

	@Test
	void createCategory() {
		//given
		Category category = Category.builder()
			.name("맛집")
			.build();
		//when
		Category savedCategory = categoryRepository.save(category);
		//then
		Assertions.assertThat(category).isSameAs(savedCategory);
	}
}