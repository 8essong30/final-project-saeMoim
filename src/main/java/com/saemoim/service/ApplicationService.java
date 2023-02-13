package com.saemoim.service;

import java.util.List;

import com.saemoim.dto.response.ApplicationResponseDto;

public interface ApplicationService {
	List<ApplicationResponseDto> getMyApplications(String username);

	void applyGroup(Long groupId, String username);

	void cancelApplication(Long applicationId, String username);

	List<ApplicationResponseDto> getApplications(Long groupId, String username);

	void permitApplication(Long applicationId, String username);

	void rejectApplication(Long applicationId, String username);
}
