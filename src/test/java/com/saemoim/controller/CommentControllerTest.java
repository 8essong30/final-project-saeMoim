package com.saemoim.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
import com.saemoim.annotation.WithCustomMockUser;
import com.saemoim.dto.request.CommentRequestDto;
import com.saemoim.dto.response.CommentResponseDto;
import com.saemoim.jwt.JwtUtil;
import com.saemoim.oauth.CustomOAuth2UserService;
import com.saemoim.oauth.OAuth2AuthenticationSuccessHandler;
import com.saemoim.security.CustomAccessDeniedHandler;
import com.saemoim.security.CustomAuthenticationEntryPoint;
import com.saemoim.service.CommentService;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(controllers = CommentController.class)
@MockBean(JpaMetamodelMappingContext.class)
class CommentControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private CommentService commentService;
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
	@WithCustomMockUser
	@DisplayName("?????? ??????")
	void getComments() throws Exception {
		// given
		var postId = 1L;
		var response = CommentResponseDto.builder()
			.id(1L)
			.userId(1L)
			.username("writer")
			.comment("comment of post")
			.createdAt(LocalDateTime.now())
			.modifiedAt(LocalDateTime.now())
			.build();
		List<CommentResponseDto> list = new ArrayList<>();
		list.add(response);
		when(commentService.getComments(anyLong())).thenReturn(list);
		// when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.get("/posts/{postId}/comment", postId));
		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("comment/getAll",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("postId").description("????????? id")
				),
				responseFields(
					subsectionWithPath("data").description("?????? ??????"),
					fieldWithPath("data.[].id").description("?????? id").type(JsonFieldType.NUMBER),
					fieldWithPath("data.[].userId").description("????????? id").type(JsonFieldType.NUMBER),
					fieldWithPath("data.[].username").description("????????? ?????????").type(JsonFieldType.STRING),
					fieldWithPath("data.[].comment").description("?????? ??????").type(JsonFieldType.STRING),
					fieldWithPath("data.[].createdAt").description("?????????").type(JsonFieldType.STRING),
					fieldWithPath("data.[].modifiedAt").description("?????????").type(JsonFieldType.STRING)
				)
			));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("?????? ??????")
	void createComment() throws Exception {
		// given
		var postId = 1L;
		CommentRequestDto request = CommentRequestDto.builder().comment("so good").build();
		// when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.post("/posts/{postId}/comment", postId)
				.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer accessToken")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new Gson().toJson(request)));
		// then
		resultActions.andExpect(status().isCreated())
			.andDo(document("comment/create",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("postId").description("????????? id")
				),
				requestHeaders(
					headerWithName("Authorization").description("???????????????")
				),
				requestFields(
					fieldWithPath("comment").description("?????? ??????").type(JsonFieldType.STRING)
				),
				responseFields(
					fieldWithPath("data").description("???????????????")
				)
			));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("?????? ??????")
	void updateComment() throws Exception {
		// given
		var commentId = 1L;
		CommentRequestDto request = CommentRequestDto.builder().comment("so good").build();
		// when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.put("/comments/{commentId}", commentId)
				.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer accessToken")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new Gson().toJson(request)));
		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("comment/update",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("commentId").description("?????? id")
				),
				requestHeaders(
					headerWithName("Authorization").description("???????????????")
				),
				requestFields(
					fieldWithPath("comment").description("?????? ??????").type(JsonFieldType.STRING)
				),
				responseFields(
					fieldWithPath("data").description("???????????????")
				)
			));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("?????? ??????")
	void deleteComment() throws Exception {
		// given
		var commentId = 1L;

		// when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.delete("/comments/{commentId}", commentId)
				.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer accessToken"));
		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("comment/delete",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("commentId").description("?????? id")
				),
				requestHeaders(
					headerWithName("Authorization").description("???????????????")
				),
				responseFields(
					fieldWithPath("data").description("???????????????").type(JsonFieldType.STRING)
				)
			));
	}
}