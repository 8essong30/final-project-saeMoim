package com.saemoim.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saemoim.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

	boolean existsByEmail(String email);

	boolean existsByUsername(String username);

	Optional<User> findByEmail(String email);

	Optional<User> findById(Long userId);

	List<User> findAllByOrderByUsername();
}
