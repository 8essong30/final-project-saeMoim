package com.saemoim.chat.controller;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.saemoim.chat.dto.ChatRequestDto;
import com.saemoim.chat.dto.ChatResponseDto;
import com.saemoim.chat.service.ChatService;
import com.saemoim.dto.response.GenericsResponseDto;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class StompChatController {
	private final SimpMessagingTemplate template; //특정 Broker로 메세지를 전달
	private final ChatService chatService;

	//stompConfig에서 설정한 applicationDestinationPrefixes 와 @MessageMapping 경로가 병합됨 (/pub + ...)
	// /pub/chat/enter 에 메세지가 오면 동작
	@MessageMapping(value = "/chat/enter")
	public void enter(ChatRequestDto message){
		ChatResponseDto welcomeMessageDto = new ChatResponseDto(
			message.getGroupId(),
			message.getUserId(),
			message.getWriter(),
			message.getWriter() + "님이 모임에 입장하셨습니다.",
			LocalDateTime.now()
		);
		template.convertAndSend("/sub/chat/group/" + welcomeMessageDto.getGroupId(), welcomeMessageDto);
	}

	// /pub/chat/message 에 메세지가 오면 동작
	@MessageMapping(value = "/chat/message")
	public void message(ChatRequestDto message){
		ChatResponseDto savedMessage = chatService.saveMessage(message);
		template.convertAndSend("/sub/chat/group/" + savedMessage.getGroupId(), savedMessage);
	}

	// 특정 그룹 채팅 불러오기
	@GetMapping("/chat/{groupId}")
	public ResponseEntity<GenericsResponseDto> getAllChatByGroupId(@PathVariable Long groupId) {
		return ResponseEntity.ok().body(new GenericsResponseDto(chatService.getAllChatByGroupId(groupId)));
	}
}