package com.saemoim.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.dto.request.ReportRequestDto;
import com.saemoim.dto.response.ReportResponseDto;
import com.saemoim.dto.response.StatusResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

	@Transactional(readOnly = true)
	@Override
	public List<ReportResponseDto> getReportedUsers() {
		return null;
	}

	@Transactional
	@Override
	public StatusResponseDto reportUser(Long subjectUserId, ReportRequestDto requestDto, String username) {
		return null;
	}

}
