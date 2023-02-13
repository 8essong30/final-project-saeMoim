package com.saemoim.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.saemoim.dto.response.StatusResponseDto;
import com.saemoim.service.BlackListServiceImpl;

@ExtendWith(MockitoExtension.class)
class BlackListControllerTest {

	@Mock
	private BlackListServiceImpl blackListService;

	@InjectMocks
	private BlackListController blackListController;

	private MockMvc mockMvc;

	@BeforeEach
	void init() {
		mockMvc = MockMvcBuilders.standaloneSetup(blackListController).build();
	}

	@Test
	@DisplayName("블랙리스트 조회")
	void getBlacklists() throws Exception {
		//given
		when(blackListService.getBlacklists()).thenReturn(new ArrayList<>());
		//when
		mockMvc.perform(MockMvcRequestBuilders.get("/blacklist"));
		//then
		verify(blackListService).getBlacklists();
	}

	@Test
	@DisplayName("블랙리스트 등록")
	void addBlacklist() throws Exception {
		//given
		StatusResponseDto responseDto = new StatusResponseDto(HttpStatus.OK, " 블랙리스트 등록 완료");
		when(blackListService.addBlacklist(anyLong())).thenReturn(responseDto);
		//when
		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/blacklist/users/{userId}", 1L));
		//then
		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("message", responseDto.getMessage()).exists());
	}

	@Test
	@DisplayName("영구 블랙리스트 등록")
	void imposePermanentBan() throws Exception {
		//given
		StatusResponseDto responseDto = new StatusResponseDto(HttpStatus.OK, " 영구 블랙리스트 등록 완료");
		when(blackListService.imposePermanentBan(anyLong())).thenReturn(responseDto);
		//when
		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.patch("/blacklist/{blacklistId}", 1L));
		//then
		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("message", responseDto.getMessage()).exists());
	}

	@Test
	@DisplayName("블랙리스트 해제")
	void deleteBlacklist() throws Exception {
		//given
		StatusResponseDto responseDto = new StatusResponseDto(HttpStatus.OK, " 블랙리스트 해제 완료");
		when(blackListService.deleteBlacklist(anyLong())).thenReturn(responseDto);
		//when
		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/blacklist/{blacklistId}", 1L));
		//then
		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("message", responseDto.getMessage()).exists());
	}
}