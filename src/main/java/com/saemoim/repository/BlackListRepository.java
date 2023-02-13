package com.saemoim.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saemoim.domain.BlackList;
import com.saemoim.domain.User;

public interface BlackListRepository extends JpaRepository<BlackList, Long> {

	boolean existsByUser(User user);

	Optional<BlackList> findByUser(User user);

	List<BlackList> findAllByOrderByCreatedAtDesc();
}
