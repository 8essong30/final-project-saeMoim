package com.saemoim.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.dto.response.ApplicationResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {
	@Transactional(readOnly = true)
	@Override
	public List<ApplicationResponseDto> getMyApplications(String username) {
		return null;
	}

	@Transactional
	@Override
	public void applyGroup(Long groupId, String username) {

	}

	@Transactional
	@Override
	public void cancelApplication(Long applicationId, String username) {

	}

	@Transactional(readOnly = true)
	@Override
	public List<ApplicationResponseDto> getApplications(Long groupId, String username) {
		return null;
	}

	@Transactional
	@Override
	public void permitApplication(Long applicationId, String username) {

	}

	@Transactional
	@Override
	public void rejectApplication(Long applicationId, String username) {

	}
}
