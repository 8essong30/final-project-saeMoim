package com.saemoim.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.dto.request.GroupRequestDto;
import com.saemoim.dto.response.GroupResponseDto;
import com.saemoim.dto.response.MessageResponseDto;
import com.saemoim.dto.response.MyGroupResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
	@Transactional(readOnly = true)
	@Override
	public List<MyGroupResponseDto> getGroups() {
		return null;
	}

	@Transactional(readOnly = true)
	@Override
	public MyGroupResponseDto getGroup(Long groupId) {
		return null;
	}

	@Transactional(readOnly = true)
	@Override
	public List<MyGroupResponseDto> getMyGroupsByLeader(String username) {
		return null;
	}

	@Transactional(readOnly = true)
	@Override
	public List<MyGroupResponseDto> getMyGroupsByParticipant(String username) {
		return null;
	}

	@Transactional
	@Override
	public GroupResponseDto createGroup(GroupRequestDto requestDto, String username) {
		return null;
	}

	@Transactional
	@Override
	public GroupResponseDto updateGroup(Long groupId, GroupRequestDto requestDto, String username) {
		return null;
	}

	@Transactional
	@Override
	public MessageResponseDto deleteGroup(Long groupId, String username) {
		return null;
	}

	@Transactional
	@Override
	public MessageResponseDto openGroup(Long groupId, String username) {
		return null;
	}

	@Transactional
	@Override
	public MessageResponseDto closeGroup(Long groupId, String username) {
		return null;
	}
}
