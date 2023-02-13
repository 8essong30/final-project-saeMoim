package com.saemoim.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saemoim.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	boolean existsByName(String name);

	boolean existsByParentId(Long id);

	List<Category> findByParentIdIsNull();

	List<Category> findByParentIdIsNotNull();

}
