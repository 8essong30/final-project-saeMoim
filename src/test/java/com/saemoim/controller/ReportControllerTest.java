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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
import com.saemoim.dto.request.ReportRequestDto;
import com.saemoim.dto.response.ReportResponseDto;
import com.saemoim.jwt.JwtUtil;
import com.saemoim.oauth.CustomOAuth2UserService;
import com.saemoim.oauth.OAuth2AuthenticationSuccessHandler;
import com.saemoim.security.CustomAccessDeniedHandler;
import com.saemoim.security.CustomAuthenticationEntryPoint;
import com.saemoim.service.ReportServiceImpl;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(controllers = ReportController.class)
@MockBean(JpaMetamodelMappingContext.class)
class ReportControllerTest {
	@Autowired
	MockMvc mockMvc;
	@MockBean
	ReportServiceImpl reportService;
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
	@DisplayName("????????? ????????? ??????")
	@WithCustomMockUser(role = UserRoleEnum.ADMIN)
	void getReportedUsers() throws Exception {
		//given
		int page = 0;
		int size = 10;
		String sort = "createdDate,desc";
		PageRequest pageable = PageRequest.of(page, size);
		ReportResponseDto report = ReportResponseDto.builder()
			.reporterName("reporter")
			.id(1L)
			.createdAt(LocalDateTime.now())
			.subjectUsername("subject")
			.subjectUserId(1L)
			.content("swearing")
			.build();
		List<ReportResponseDto> list = new ArrayList<>();
		list.add(report);
		Page<ReportResponseDto> pageDto = new PageImpl<>(list, pageable, list.size());
		when(reportService.getReportedUsers(any(Pageable.class))).thenReturn(pageDto);
		//when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get("/admin/report")
			.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer accessToken")
			.param("page", String.valueOf(page)).param("size", String.valueOf(size))
			.param("sort", sort).contentType(MediaType.APPLICATION_JSON));
		//then
		resultActions.andExpect(status().isOk())
			.andDo(document("report/getAll",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestHeaders(
					headerWithName("Authorization").description("????????????????????????")
				),
				pathParameters(
					parameterWithName("page").description("????????? ?????????").optional(),
					parameterWithName("size").description("????????? ??? ?????? ???").optional(),
					parameterWithName("sort").description("?????? ??????")
						.optional()),
				responseFields(
					subsectionWithPath("data").description("?????????"),
					subsectionWithPath("data.content").description("????????????"),
					fieldWithPath("data.content.[].id").description("?????? id").type(JsonFieldType.NUMBER),
					fieldWithPath("data.content.[].subjectUserId").description("???????????? id").type(JsonFieldType.NUMBER),
					fieldWithPath("data.content.[].subjectUsername").description("???????????? ?????????").type(JsonFieldType.STRING),
					fieldWithPath("data.content.[].reporterName").description("????????? ?????????").type(JsonFieldType.STRING),
					fieldWithPath("data.content.[].content").description("?????? ??????").type(JsonFieldType.STRING),
					fieldWithPath("data.content.[].createdAt").description("????????????").type(JsonFieldType.STRING)
				)
			));
	}

	@Test
	@DisplayName("????????? ??????")
	@WithCustomMockUser
	void reportUser() throws Exception {
		//given
		Long subjectUserId = 1L;
		ReportRequestDto requestDto = ReportRequestDto.builder().content("????????????").build();
		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.post("/report/users/{subjectUserId}", subjectUserId)
				.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer accessToken")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new Gson().toJson(requestDto)));
		//then
		resultActions.andExpect(status().isCreated()).andExpect(jsonPath("data").value("????????? ????????? ?????? ???????????????."))
			.andDo(document("report/do",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestHeaders(
					headerWithName("Authorization").description("???????????????")
				),
				pathParameters(
					parameterWithName("subjectUserId").description("?????? ??? ?????? id")
				),
				requestFields(
					fieldWithPath("content").description("?????? ??????").type(JsonFieldType.STRING)
				),
				responseFields(
					fieldWithPath("data").description("?????? ?????????").type(JsonFieldType.STRING)
				)
			));

	}

	@Test
	@DisplayName("???????????? ??????")
	@WithCustomMockUser(role = UserRoleEnum.ADMIN)
	void deleteReport() throws Exception {
		//given
		var reportId = 1L;
		doNothing().when(reportService).deleteReport(anyLong());
		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.delete("/admin/reports/{reportId}", reportId)
				.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer accessToken"));
		//then
		resultActions.andExpect(status().isOk()).andExpect(jsonPath("data").value("?????? ????????? ?????? ???????????????."))
			.andDo(document("report/delete",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("reportId").description("?????? ??? ?????? id")
				),
				requestHeaders(
					headerWithName("Authorization").description("????????????????????????")
				),
				responseFields(
					fieldWithPath("data").description("?????? ?????????").type(JsonFieldType.STRING)
				)
			));
	}
}