package com.saemoim.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.domain.BlackList;
import com.saemoim.domain.User;
import com.saemoim.domain.enums.BlacklistStatusEnum;
import com.saemoim.domain.enums.UserRoleEnum;
import com.saemoim.dto.response.BlackListResponseDto;
import com.saemoim.exception.ErrorCode;
import com.saemoim.repository.BlackListRepository;
import com.saemoim.repository.ReportRepository;
import com.saemoim.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BlackListServiceImpl implements BlackListService {
	private final UserRepository userRepository;
	private final BlackListRepository blackListRepository;
	private final ReportRepository reportRepository;

	@Transactional(readOnly = true)
	@Override
	public List<BlackListResponseDto> getBlacklists() {
		return blackListRepository.findAllByOrderByCreatedAtDesc().stream().map(BlackListResponseDto::new).toList();
	}

	@Transactional
	@Override
	public void addBlacklist(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage())
		);

		if (blackListRepository.existsByUser(user)) {
			throw new IllegalArgumentException(ErrorCode.DUPLICATED_BLACKLIST.getMessage());
		}

		user.plusBanCount();
		BlacklistStatusEnum status;
		if (user.getBanCount() > 2) {
			status = BlacklistStatusEnum.PERMANENT_BAN;
			reportRepository.deleteAllBySubject_Id(userId);
		} else {
			status = BlacklistStatusEnum.BAN;
		}

		user.updateStatus(UserRoleEnum.REPORT);
		BlackList blackList = new BlackList(user, status);
		blackListRepository.save(blackList);
	}

	@Transactional
	@Override
	public void imposePermanentBan(Long blacklistId) {
		BlackList find = blackListRepository.findById(blacklistId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.DUPLICATED_BLACKLIST.getMessage())
		);

		find.updateStatus(BlacklistStatusEnum.PERMANENT_BAN);

		blackListRepository.save(find);
		reportRepository.deleteAllBySubject_Id(find.getUserId());
	}

	@Transactional
	@Override
	public void deleteBlacklist(Long blacklistId) {
		BlackList blackList = blackListRepository.findById(blacklistId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_BLACKLIST.getMessage())
		);
		User user = userRepository.findById(blackList.getUserId()).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage())
		);
		user.updateStatus(UserRoleEnum.USER);
		blackListRepository.delete(blackList);
	}

	@Transactional
	@Scheduled(cron = "${schedules.cron.reward.publish}")
	public void scheduledBlacklist() {
		List<BlackList> blackLists = blackListRepository.findAll()
			.stream()
			.filter(b -> b.getStatus().equals(BlacklistStatusEnum.BAN))
			.filter(b -> LocalDateTime.now().isAfter(b.getCreatedAt().plusDays(7)))
			.toList();

		if (blackLists.size() > 0) {
			blackListRepository.deleteBlackLists(blackLists);
		}
	}
}
