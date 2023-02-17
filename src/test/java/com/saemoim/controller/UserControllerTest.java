package com.saemoim.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.google.gson.Gson;
import com.saemoim.annotation.WithCustomMockUser;
import com.saemoim.domain.User;
import com.saemoim.domain.enums.UserRoleEnum;
import com.saemoim.dto.request.SignInRequestDto;
import com.saemoim.dto.request.SignUpRequestDto;
import com.saemoim.dto.request.WithdrawRequestDto;
import com.saemoim.dto.response.TokenResponseDto;
import com.saemoim.dto.response.UserResponseDto;
import com.saemoim.jwt.JwtUtil;
import com.saemoim.service.UserServiceImpl;

import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
@MockBean(JpaMetamodelMappingContext.class)
class UserControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private UserServiceImpl userService;

	@Test
	@WithCustomMockUser
	@DisplayName("회원 가입")
	void signUp() throws Exception {
		//given
		SignUpRequestDto requestDto = SignUpRequestDto.builder()
			.username("장성준")
			.password("asdf1234!")
			.email("aaaaa@naver.com")
			.build();
		doNothing().when(userService).signUp(any(SignUpRequestDto.class));
		//when
		ResultActions resultActions = mockMvc.perform(post("/sign-up")
			.contentType(MediaType.APPLICATION_JSON)
			.content(new Gson().toJson(requestDto))
			.with(csrf()));
		//then
		resultActions.andExpect(status().isOk()).andExpect(jsonPath("message").value("회원가입 완료"));
	}

	@Test
	@DisplayName("로그인")
	@WithCustomMockUser
	void signIn() throws Exception {
		//given
		TokenResponseDto responseDto = mock(TokenResponseDto.class);
		SignInRequestDto requestDto = SignInRequestDto.builder()
			.email("asdfs@naver.com")
			.password("asdf1234!")
			.build();

		when(responseDto.getAccessToken()).thenReturn("accessToken");
		when(responseDto.getRefreshToken()).thenReturn("refreshToken");
		when(userService.signIn(any(SignInRequestDto.class))).thenReturn(responseDto);
		//when
		ResultActions resultActions = mockMvc.perform(post("/sign-in")
			.contentType(MediaType.APPLICATION_JSON)
			.with(csrf())
			.content(new Gson().toJson(requestDto)));
		//then
		resultActions.andExpect(status().isOk()).andExpect(header().string("Authorization", "accessToken"))
			.andExpect(jsonPath("message").value("로그인 완료"));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("로그아웃")
	void logout() throws Exception {
		//given
		doNothing().when(userService).logout(anyString());

		//when
		ResultActions resultActions = mockMvc.perform(post("/log-out")
			.header(JwtUtil.AUTHORIZATION_HEADER, "accessToken")
			.header(JwtUtil.REFRESH_TOKEN_HEADER, "refreshToken")
			.contentType(MediaType.APPLICATION_JSON)
			.with(csrf()));

		//then
		resultActions.andExpect(status().isOk()).andExpect(header().string("Authorization", ""))
			.andExpect(jsonPath("message").value("로그아웃 완료"));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("회원 탈퇴")
	void withdraw() throws Exception {
		//given
		WithdrawRequestDto requestDto = WithdrawRequestDto.builder()
			.password("asdf1234!")
			.build();

		doNothing().when(userService).withdraw(requestDto, 1L, "refreshToken");
		//when
		ResultActions resultActions = mockMvc.perform(delete("/withdrawal")
			.header(JwtUtil.REFRESH_TOKEN_HEADER,"refreshToken")
			.contentType(MediaType.APPLICATION_JSON)
			.content(new Gson().toJson(requestDto))
			.with(csrf()));
		//then
		resultActions.andExpect(status().isOk()).andExpect(header().string("Authorization", ""))
			.andExpect(jsonPath("message").value("회원탈퇴 완료"));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("전체 회원 조회")
	void getAllUsers() throws Exception {
		//given
		List<UserResponseDto> list = new ArrayList<>();
		User user = User.builder()
			.id(1L)
			.banCount(0)
			.content("asdfasfsdfsaf")
			.email("aaaaa@naver.com")
			.password("aaasdf1234!")
			.role(UserRoleEnum.ADMIN)
			.username("장성준")
			.build();

		UserResponseDto responseDto = new UserResponseDto(user);
		list.add(responseDto);

		when(userService.getAllUsers()).thenReturn(list);
		//when
		ResultActions resultActions = mockMvc.perform(get("/admin/user"));
		//then
		resultActions.andExpect(jsonPath("$[0]['username']").value("장성준"));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("리프레쉬 토큰 재발급")
	void reissueSuccess() throws Exception {
		// given
		String accessToken = "accessToken";
		String refreshToken = "refreshToken";
		TokenResponseDto tokenResponseDto = new TokenResponseDto(accessToken, refreshToken);
		when(userService.reissueToken(anyString(), anyString())).thenReturn(tokenResponseDto);

		// when
		ResultActions resultActions = mockMvc.perform(post("/reissue")
			.header(JwtUtil.AUTHORIZATION_HEADER, accessToken)
			.header(JwtUtil.REFRESH_TOKEN_HEADER, refreshToken)
			.with(csrf()));

		// then
		resultActions.andExpect(status().isOk())
			.andExpect(header().string(JwtUtil.AUTHORIZATION_HEADER, accessToken))
			.andExpect(header().string(JwtUtil.REFRESH_TOKEN_HEADER, refreshToken))
			.andExpect(jsonPath("$.message").value("토큰 재발급 완료"));
	}

}