package com.saemoim.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.domain.Report;
import com.saemoim.domain.User;
import com.saemoim.dto.request.ReportRequestDto;
import com.saemoim.dto.response.ReportResponseDto;
import com.saemoim.exception.ErrorCode;
import com.saemoim.repository.ReportRepository;
import com.saemoim.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
	private final ReportRepository reportRepository;
	private final UserRepository userRepository;

	@Transactional(readOnly = true)
	@Override
	public Page<ReportResponseDto> getReportedUsers(Pageable pageable) {
		return reportRepository.findAllByOrderByCreatedAt(pageable).map(ReportResponseDto::new);
	}

	@Transactional
	@Override
	public void reportUser(Long subjectUserId, ReportRequestDto requestDto, String reporterName) {
		User subjectUser = userRepository.findById(subjectUserId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage())
		);

		if (reportRepository.existsByReporterNameAndSubject(reporterName, subjectUser)) {
			throw new IllegalArgumentException(ErrorCode.DUPLICATED_REPORT.getMessage());
		}

		reportRepository.save(new Report(subjectUser, reporterName, requestDto.getContent()));
	}

	@Transactional
	@Override
	public void deleteReport(Long reportId) {
		Report report = reportRepository.findById(reportId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_REPORT.getMessage())
		);

		reportRepository.delete(report);
	}
}
