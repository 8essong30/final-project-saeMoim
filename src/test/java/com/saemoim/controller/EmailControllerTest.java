package com.saemoim.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.saemoim.dto.request.EmailCodeRequestDto;
import com.saemoim.dto.request.EmailRequestDto;
import com.saemoim.jwt.JwtUtil;
import com.saemoim.oauth.CustomOAuth2UserService;
import com.saemoim.oauth.OAuth2AuthenticationSuccessHandler;
import com.saemoim.security.CustomAccessDeniedHandler;
import com.saemoim.security.CustomAuthenticationEntryPoint;
import com.saemoim.service.EmailService;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(controllers = EmailController.class)
@MockBean(JpaMetamodelMappingContext.class)
class EmailControllerTest {

	@Autowired
	MockMvc mockMvc;
	@MockBean
	EmailService emailService;
	@MockBean
	private JwtUtil jwtUtil;
	@MockBean
	private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
	@MockBean
	private CustomAccessDeniedHandler customAccessDeniedHandler;
	@MockBean
	private CustomOAuth2UserService oAuth2UserService;
	@MockBean
	private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

	@BeforeEach
	public void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
			.apply(documentationConfiguration(restDocumentation)).build();
	}

	@Test
	@DisplayName("????????? ???????????? ??????")
	void checkEmailAuthCode() throws Exception {
		// given
		EmailCodeRequestDto request = new EmailCodeRequestDto("email@eamil.com", "code");
		// when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.post("/email/auth-code")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new Gson().toJson(request)));
		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("email/check-code",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("email").description("???????????? ???????????????").type(JsonFieldType.STRING),
					fieldWithPath("code").description("???????????? ?????? ??????").type(JsonFieldType.STRING)
				),
				responseFields(
					fieldWithPath("data").description("?????? ?????????").type(JsonFieldType.STRING)
				)
			));
	}

	@Test
	@DisplayName("????????? ???????????? ??????")
	void sendEmailAuthCode() throws Exception {
		// given
		EmailRequestDto request = EmailRequestDto.builder().email("email@email.com").build();
		// when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.post("/email")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new Gson().toJson(request)));
		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("email/send-code",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("email").description("???????????? ???????????????").type(JsonFieldType.STRING)
				),
				responseFields(
					fieldWithPath("data").description("?????? ?????????").type(JsonFieldType.STRING)
				)
			));
	}

	@Test
	@DisplayName("?????? ???????????? ??????")
	void sendTempPassword() throws Exception {
		// given
		EmailRequestDto request = EmailRequestDto.builder().email("email@email.com").build();
		// when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.put("/email/password")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new Gson().toJson(request)));
		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("email/send-pwd",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("email").description("???????????? ???????????????").type(JsonFieldType.STRING)
				),
				responseFields(
					fieldWithPath("data").description("?????? ?????????").type(JsonFieldType.STRING)
				)
			));
	}
}