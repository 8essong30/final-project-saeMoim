package com.saemoim.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.domain.BlackList;
import com.saemoim.domain.User;
import com.saemoim.domain.enums.BlacklistStatusEnum;
import com.saemoim.domain.enums.UserRoleEnum;
import com.saemoim.dto.response.BlackListResponseDto;
import com.saemoim.dto.response.StatusResponseDto;
import com.saemoim.exception.ErrorCode;
import com.saemoim.repository.BlackListRepository;
import com.saemoim.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BlackListServiceImpl implements BlackListService {
	private final UserRepository userRepository;
	private final BlackListRepository blackListRepository;

	@Transactional(readOnly = true)
	@Override
	public List<BlackListResponseDto> getBlacklists() {
		return blackListRepository.findAllByOrderByCreatedAtDesc().stream().map(BlackListResponseDto::new).toList();
	}

	@Transactional
	@Override
	public StatusResponseDto addBlacklist(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage())
		);

		if (blackListRepository.existsByUser(user)) {
			throw new IllegalArgumentException(ErrorCode.DUPLICATED_BLACKLIST.getMessage());
		}

		user.plusBanCount();
		BlacklistStatusEnum status =
			user.getBanCount() > 2 ? BlacklistStatusEnum.PERMANENT_BAN : BlacklistStatusEnum.BAN;
		user.updateStatus(UserRoleEnum.REPORT);
		BlackList blackList = new BlackList(user, status);

		blackListRepository.save(blackList);
		return new StatusResponseDto(HttpStatus.OK, user.getUsername() + " 블랙리스트 등록 완료");
	}

	@Transactional
	@Override
	public StatusResponseDto imposePermanentBan(Long blacklistId) {
		BlackList find = blackListRepository.findById(blacklistId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.DUPLICATED_BLACKLIST.getMessage())
		);

		find.updateStatus(BlacklistStatusEnum.PERMANENT_BAN);

		blackListRepository.save(find);
		return new StatusResponseDto(HttpStatus.OK, find.getUsername() + " 영구 블랙리스트 등록 완료");
	}

	@Transactional
	@Override
	public StatusResponseDto deleteBlacklist(Long blacklistId) {
		BlackList blackList = blackListRepository.findById(blacklistId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_EXIST_BLACKLIST.getMessage())
		);

		String name = blackList.getUsername();

		blackListRepository.delete(blackList);
		return new StatusResponseDto(HttpStatus.OK, name + " 블랙리스트 해제 완료");
	}

	@Scheduled(cron = "${schedules.cron.reward.publish}")
	@Override
	public void scheduledBlacklist() {
		blackListRepository.findAll()
			.stream()
			.filter(b -> b.getStatus().equals(BlacklistStatusEnum.BAN))
			.filter(b -> LocalDateTime.now().isAfter(b.getCreatedAt().plusDays(7)))
			.forEach(blackListRepository::delete);
	}
}
