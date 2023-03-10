package com.saemoim.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
import com.saemoim.domain.enums.UserRoleEnum;
import com.saemoim.dto.request.ReviewRequestDto;
import com.saemoim.dto.response.ReviewResponseDto;
import com.saemoim.jwt.JwtUtil;
import com.saemoim.oauth.CustomOAuth2UserService;
import com.saemoim.oauth.OAuth2AuthenticationSuccessHandler;
import com.saemoim.security.CustomAccessDeniedHandler;
import com.saemoim.security.CustomAuthenticationEntryPoint;
import com.saemoim.service.ReviewService;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(controllers = ReviewController.class)
@MockBean(JpaMetamodelMappingContext.class)
class ReviewControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private ReviewService reviewService;
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
	@DisplayName("?????? ?????? ??????")
	void getReviews() throws Exception {
		// given
		var groupId = 1L;
		var response = ReviewResponseDto.builder()
			.id(1L)
			.userId(1L)
			.username("writer")
			.content("content of post")
			.build();
		List<ReviewResponseDto> list = new ArrayList<>();
		list.add(response);
		Page<ReviewResponseDto> page = new PageImpl<>(list);

		when(reviewService.getReviews(anyLong(), any(Pageable.class))).thenReturn(page);

		// when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.get("/groups/{groupId}/review", groupId));
		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("review/getAll",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("groupId").description("?????? id")
				),
				responseFields(
					subsectionWithPath("data").description("?????????"),
					subsectionWithPath("data.content").description("????????? ??????"),
					fieldWithPath("data.content.[].id").description("????????? id").type(JsonFieldType.NUMBER),
					fieldWithPath("data.content.[].userId").description("????????? id").type(JsonFieldType.NUMBER),
					fieldWithPath("data.content.[].username").description("????????? ?????????").type(JsonFieldType.STRING),
					fieldWithPath("data.content.[].content").description("????????? ??????").type(JsonFieldType.STRING)
				)
			));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("????????????")
	void createReview() throws Exception {
		// given
		var groupId = 1L;
		ReviewRequestDto request = ReviewRequestDto.builder().content("so good").build();
		// when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.post("/groups/{groupId}/review", groupId)
				.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer accessToken")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new Gson().toJson(request)));
		// then
		resultActions.andExpect(status().isCreated())
			.andDo(document("review/create",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("groupId").description("?????? id")
				),
				requestHeaders(
					headerWithName("Authorization").description("???????????????")
				),
				requestFields(
					fieldWithPath("content").description("?????? ??????").type(JsonFieldType.STRING)
				),
				responseFields(
					fieldWithPath("data").description("???????????????")
				)
			));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("?????? ??????")
	void updateReview() throws Exception {
		// given
		var reviewId = 1L;
		ReviewRequestDto request = ReviewRequestDto.builder().content("so so good").build();
		// when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.put("/reviews/{reviewId}", reviewId)
				.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer accessToken")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new Gson().toJson(request)));
		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("review/update",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("reviewId").description("?????? id")
				),
				requestHeaders(
					headerWithName("Authorization").description("???????????????")
				),
				requestFields(
					fieldWithPath("content").description("?????? ?????? ??????").type(JsonFieldType.STRING)
				),
				responseFields(
					fieldWithPath("data").description("???????????????")
				)
			));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("?????? ??????")
	void deleteReview() throws Exception {
		// given
		var reviewId = 1L;

		// when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.delete("/reviews/{reviewId}", reviewId)
				.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer accessToken"));

		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("review/delete",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("reviewId").description("?????? id")
				),
				requestHeaders(
					headerWithName("Authorization").description("???????????????")
				),
				responseFields(
					fieldWithPath("data").description("???????????????")
				)
			));
	}

	@Test
	@WithCustomMockUser(role = UserRoleEnum.ADMIN)
	@DisplayName("????????? - ?????? ??????")
	void deleteReviewByAdmin() throws Exception {
		// given
		var reviewId = 1L;

		// when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.delete("/admin/reviews/{reviewId}", reviewId)
				.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer accessToken"));

		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("review/delete-admin",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("reviewId").description("?????? id")
				),
				requestHeaders(
					headerWithName("Authorization").description("???????????????")
				),
				responseFields(
					fieldWithPath("data").description("???????????????")
				)
			));
	}
}