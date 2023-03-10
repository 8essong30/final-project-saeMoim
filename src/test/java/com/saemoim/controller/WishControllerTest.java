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
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.saemoim.annotation.WithCustomMockUser;
import com.saemoim.domain.enums.GroupStatusEnum;
import com.saemoim.dto.response.GroupResponseDto;
import com.saemoim.jwt.JwtUtil;
import com.saemoim.oauth.CustomOAuth2UserService;
import com.saemoim.oauth.OAuth2AuthenticationSuccessHandler;
import com.saemoim.security.CustomAccessDeniedHandler;
import com.saemoim.security.CustomAuthenticationEntryPoint;
import com.saemoim.service.WishService;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(controllers = WishController.class)
@MockBean(JpaMetamodelMappingContext.class)
class WishControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private WishService wishService;
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
	@DisplayName("???????????? ??????")
	void getWishGroups() throws Exception {
		// given
		var response = GroupResponseDto.builder()
			.id(1L)
			.tags(new ArrayList<>())
			.categoryName("category")
			.groupName("moim")
			.userId(1L)
			.username("leader")
			.content("???????????????")
			.address("????????? ????????? ?????????")
			.firstRegion("?????????")
			.secondRegion("?????????")
			.latitude("??????")
			.longitude("??????")
			.status(GroupStatusEnum.OPEN)
			.wishCount(2)
			.recruitNumber(3)
			.participantCount(1)
			.imagePath("image path")
			.build();
		List<GroupResponseDto> list = new ArrayList<>();
		list.add(response);
		when(wishService.getWishGroups(anyLong())).thenReturn(list);
		// when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get("/group/wish")
			.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer accessToken"));
		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("wish/getAll",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestHeaders(
					headerWithName("Authorization").description("???????????????")
				),
				responseFields(
					subsectionWithPath("data").description("??????????????????"),
					fieldWithPath("data.[].id").description("?????? id").type(JsonFieldType.NUMBER),
					subsectionWithPath("data.[].tags").description("??????????????????"),
					fieldWithPath("data.[].categoryName").description("???????????? ??????").type(JsonFieldType.STRING),
					fieldWithPath("data.[].groupName").description("?????? ??????").type(JsonFieldType.STRING),
					fieldWithPath("data.[].userId").description("?????? ?????? id").type(JsonFieldType.NUMBER),
					fieldWithPath("data.[].username").description("?????? ?????? ?????????").type(JsonFieldType.STRING),
					fieldWithPath("data.[].content").description("?????? ?????????").type(JsonFieldType.STRING),
					fieldWithPath("data.[].address").description("?????? ??????").type(JsonFieldType.STRING),
					fieldWithPath("data.[].firstRegion").description("?????? ??????; ???/???").type(JsonFieldType.STRING),
					fieldWithPath("data.[].secondRegion").description("?????? ??????; ???/???/???")
						.type(JsonFieldType.STRING),
					fieldWithPath("data.[].latitude").description("?????? ??????; ??????").type(JsonFieldType.STRING),
					fieldWithPath("data.[].longitude").description("?????? ??????; ??????").type(JsonFieldType.STRING),
					fieldWithPath("data.[].status").description("?????? ??????(OPEN, CLOSE)")
						.type(JsonFieldType.STRING),
					fieldWithPath("data.[].wishCount").description("????????? ??????").type(JsonFieldType.NUMBER),
					fieldWithPath("data.[].recruitNumber").description("????????????").type(JsonFieldType.NUMBER),
					fieldWithPath("data.[].participantCount").description("????????????").type(JsonFieldType.NUMBER),
					fieldWithPath("data.[].imagePath").description("????????? ?????? ?????? ??????").type(JsonFieldType.STRING)
				)
			));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("??????????????? ??????")
	void wishGroup() throws Exception {
		// given
		var groupId = 1L;
		doNothing().when(wishService).addWishGroup(anyLong(), anyLong());
		// when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.post("/groups/{groupId}/wish", groupId)
				.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer accessToken"));
		// then
		resultActions.andExpect(status().isCreated())
			.andDo(document("wish/wish",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("groupId").description("?????? id")
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
	@WithCustomMockUser
	@DisplayName("???????????? ??????")
	void deleteWishGroup() throws Exception {
		// given
		var groupId = 1L;
		doNothing().when(wishService).deleteWishGroup(anyLong(), anyLong());
		// when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.delete("/groups/{groupId}/wish", groupId)
				.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer accessToken"));
		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("wish/delete",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("groupId").description("?????? id")
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