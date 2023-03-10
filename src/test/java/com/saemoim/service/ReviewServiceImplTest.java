package com.saemoim.service;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.saemoim.domain.Group;
import com.saemoim.domain.Participant;
import com.saemoim.domain.Review;
import com.saemoim.domain.User;
import com.saemoim.dto.request.ReviewRequestDto;
import com.saemoim.dto.response.ReviewResponseDto;
import com.saemoim.repository.ParticipantRepository;
import com.saemoim.repository.ReviewRepository;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {
	@Mock
	private ReviewRepository reviewRepository;
	@Mock
	private ParticipantRepository participantRepository;
	@InjectMocks
	private ReviewServiceImpl reviewService;

	@Test
	@DisplayName("후기조회")
	void getReviews() {
		// given
		var groupId = 1L;
		List<Review> list = new ArrayList<>();
		Pageable pageable = PageRequest.of(0, 10);
		Page<Review> page = new PageImpl<>(list, pageable, list.size());
		when(reviewRepository.findAllByGroup_IdOrderByCreatedAtDesc(groupId, pageable)).thenReturn(page);
		// when
		Page<ReviewResponseDto> page1 = reviewService.getReviews(groupId, pageable);
		// then
		verify(reviewRepository).findAllByGroup_IdOrderByCreatedAtDesc(groupId, pageable);
	}

	@Test
	@DisplayName("후기작성")
	void createReview() {
		// given
		var groupId = 1L;
		var request = ReviewRequestDto.builder().content("내용").build();
		var userId = 1L;
		var participant = mock(Participant.class);
		var user = mock(User.class);
		var group = mock(Group.class);

		when(participant.getGroup()).thenReturn(group);
		when(participant.getUser()).thenReturn(user);
		when(participantRepository.findByGroup_IdAndUser_Id(anyLong(), anyLong())).thenReturn(Optional.of(participant));
		// when
		reviewService.createReview(groupId, request, userId);
		// then
		verify(reviewRepository).save(any(Review.class));
	}

	@Test
	@DisplayName("후기수정")
	void updateReview() {
		// given
		Long id = 1L;
		ReviewRequestDto request = ReviewRequestDto.builder().content("내용").build();
		Long userId = 1L;
		Review review = mock(Review.class);

		when(review.isReviewWriter(userId)).thenReturn(true);
		doNothing().when(review).update(anyString());
		when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(review));

		// when
		reviewService.updateReview(id, request, userId);
		// then
		verify(reviewRepository).save(review);
	}

	@Test
	@DisplayName("후기삭제")
	void deleteReview() {
		// given
		var id = 1L;
		var userId = 1L;
		var review = mock(Review.class);

		when(review.isReviewWriter(userId)).thenReturn(true);
		when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(review));
		// when
		reviewService.deleteReview(id, userId);
		// then
		verify(reviewRepository).delete(review);
	}

	@Test
	@DisplayName("관리자 후기삭제")
	void deleteReviewByAdmin() {
		// given
		Review review = mock(Review.class);

		when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(review));
		// when
		reviewService.deleteReviewByAdmin(anyLong());
		// then
		verify(reviewRepository).delete(review);
	}
}