package com.saemoim.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LikeRepositoryTest {

	@Autowired
	LikeRepository likeRepository;

	@Test
	void existsByReview_IdAndUser_Id() {
		likeRepository.existsByPost_IdAndUserId(null, null);
	}
}