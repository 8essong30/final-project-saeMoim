package com.saemoim.service;

import java.util.List;

import com.saemoim.dto.request.ReportRequestDto;
import com.saemoim.dto.response.ReportResponseDto;

public interface ReportService {

	List<ReportResponseDto> getReportedUsers();

	void reportUser(Long subjectUserId, ReportRequestDto requestDto, String username);

}
