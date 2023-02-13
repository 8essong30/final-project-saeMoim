package com.saemoim.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParticipantServiceImpl implements ParticipantService {
	@Transactional
	@Override
	public void withdrawGroup(Long participantId, String username) {

	}
}
