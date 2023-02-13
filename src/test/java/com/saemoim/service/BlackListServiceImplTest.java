package com.saemoim.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.saemoim.domain.BlackList;
import com.saemoim.domain.User;
import com.saemoim.domain.enums.BlacklistStatusEnum;
import com.saemoim.domain.enums.UserRoleEnum;
import com.saemoim.dto.response.StatusResponseDto;
import com.saemoim.repository.BlackListRepository;
import com.saemoim.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class BlackListServiceImplTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private BlackListRepository blackListRepository;

	@InjectMocks
	private BlackListServiceImpl blackListService;

	@Test
	@DisplayName("블랙리스트 조회")
	void getBlacklists() {
		//given
		List<BlackList> list = new ArrayList<>();
		when(blackListRepository.findAllByOrderByCreatedAtDesc()).thenReturn(list);
		//when
		blackListService.getBlacklists();
		//then
		verify(blackListRepository).findAllByOrderByCreatedAtDesc();
	}

	@Test
	@DisplayName("블랙리스트 등록")
	void addBlacklist() {
		//given
		Long userId = 1L;
		User user = new User("seongjuni1@naver.com", "1234", "jun", UserRoleEnum.USER);

		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
		when(blackListRepository.existsByUser(any(User.class))).thenReturn(false);
		//when
		StatusResponseDto responseDto = blackListService.addBlacklist(userId);
		//then
		Assertions.assertThat(responseDto.getMessage()).isEqualTo(user.getUsername() + " 블랙리스트 등록 완료");
	}

	@Test
	@DisplayName("영구 블랙리스트 등록")
	void imposePermanentBan() {
		//given
		Long blacklistId = 1L;
		User user = new User("seongjuni1@naver.com", "1234", "jun", UserRoleEnum.USER);

		BlackList blackList = new BlackList(user, BlacklistStatusEnum.BAN);

		when(blackListRepository.findById(anyLong())).thenReturn(Optional.of(blackList));
		//when
		StatusResponseDto responseDto = blackListService.imposePermanentBan(blacklistId);
		//then
		Assertions.assertThat(responseDto.getMessage()).isEqualTo(user.getUsername() + " 영구 블랙리스트 등록 완료");
	}

	@Test
	@DisplayName("블랙리스트 해제")
	void deleteBlacklist() {
		//given
		Long blacklistId = 1L;
		String name = "jun";
		User user = new User("seongjuni1@naver.com", "1234", "jun", UserRoleEnum.USER);
		BlackList blacklist = new BlackList(user, BlacklistStatusEnum.BAN);

		when(blackListRepository.findById(anyLong())).thenReturn(Optional.of(blacklist));
		doNothing().when(blackListRepository).delete(any(BlackList.class));
		//when
		StatusResponseDto responseDto = blackListService.deleteBlacklist(blacklistId);
		//then
		Assertions.assertThat(responseDto.getMessage()).isEqualTo(name + " 블랙리스트 해제 완료");
	}
}