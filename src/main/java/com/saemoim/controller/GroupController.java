package com.saemoim.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.saemoim.dto.request.GroupRequestDto;
import com.saemoim.dto.response.GroupResponseDto;
import com.saemoim.dto.response.MessageResponseDto;
import com.saemoim.dto.response.MyGroupResponseDto;
import com.saemoim.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class GroupController {

	// 모든 모임 조회
	@GetMapping("/group")
	public List<MyGroupResponseDto> getGroups() {
		return null;
	}

	// 선택 모임 조회
	@GetMapping("/groups/{groupId}")
	public MyGroupResponseDto getGroup(@PathVariable Long groupId) {
		return null;
	}

	// 내가 만든 모임 조회
	@GetMapping("/leader/group")
	public List<MyGroupResponseDto> getMyGroupsByLeader(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return null;
	}

	// 참여중인 모임 조회
	@GetMapping("/participant/group")
	public List<MyGroupResponseDto> getMyGroupsByParticipant(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return null;
	}

	// 모임 생성
	@PostMapping("/group")
	public GroupResponseDto createGroup(@Validated @RequestBody GroupRequestDto requestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return null;
	}

	// 모임 수정
	@PutMapping("/groups/{groupId}")
	public GroupResponseDto updateGroup(@PathVariable Long groupId,
		@Validated @RequestBody GroupRequestDto requestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return null;
	}

	// 모임 삭제
	@DeleteMapping("/groups/{groupId}")
	public MessageResponseDto deleteGroup(@PathVariable Long groupId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return null;
	}

	// 모임 열기
	@PatchMapping("/groups/{groupId}/open")
	public MessageResponseDto openGroup(@PathVariable Long groupId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return null;
	}

	// 모임 닫기
	@PatchMapping("/groups/{groupId}/close")
	public MessageResponseDto closeGroup(@PathVariable Long groupId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return null;
	}

}
