package com.saemoim.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.generate.RestDocumentationGenerator;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.saemoim.annotation.WithCustomMockUser;
import com.saemoim.domain.enums.UserRoleEnum;
import com.saemoim.dto.response.PostResponseDto;
import com.saemoim.fileUpload.AWSS3Uploader;
import com.saemoim.jwt.JwtUtil;
import com.saemoim.oauth.CustomOAuth2UserService;
import com.saemoim.oauth.OAuth2AuthenticationSuccessHandler;
import com.saemoim.security.CustomAccessDeniedHandler;
import com.saemoim.security.CustomAuthenticationEntryPoint;
import com.saemoim.service.PostService;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(controllers = PostController.class)
@MockBean(JpaMetamodelMappingContext.class)
class PostControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private PostService postService;
	@MockBean
	private AWSS3Uploader awsS3Uploader;
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
	@DisplayName("?????? ????????? ??????")
	void getAllPostsByGroup() throws Exception {
		// given
		var groupId = 1L;
		var response = PostResponseDto.builder()
			.id(1L)
			.title("title of post")
			.userId(1L)
			.username("writer")
			.content("content of post")
			.createdAt(LocalDateTime.now())
			.modifiedAt(LocalDateTime.now())
			.likeCount(0)
			.isLikeChecked(false)
			.imagePath("image path")
			.build();
		List<PostResponseDto> list = new ArrayList<>();
		list.add(response);
		Page<PostResponseDto> page = new PageImpl<>(list);

		when(postService.getAllPostsByGroup(anyLong(), any(Pageable.class), anyLong())).thenReturn(page);
		// when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.get("/groups/{groupId}/post", groupId)
				.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer accessToken"));
		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("post/getAll",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestHeaders(
					headerWithName("Authorization").description("???????????????")
				),
				pathParameters(
					parameterWithName("groupId").description("?????? id")
				),
				responseFields(
					subsectionWithPath("content").description("????????? ??????"),
					fieldWithPath("content.[].id").description("????????? id").type(JsonFieldType.NUMBER),
					fieldWithPath("content.[].title").description("????????? ??????").type(JsonFieldType.STRING),
					fieldWithPath("content.[].userId").description("????????? id").type(JsonFieldType.NUMBER),
					fieldWithPath("content.[].username").description("????????? ?????????").type(JsonFieldType.STRING),
					fieldWithPath("content.[].content").description("????????? ??????").type(JsonFieldType.STRING),
					fieldWithPath("content.[].createdAt").description("?????????").type(JsonFieldType.STRING),
					fieldWithPath("content.[].modifiedAt").description("?????????").type(JsonFieldType.STRING),
					fieldWithPath("content.[].likeCount").description("????????? ???").type(JsonFieldType.NUMBER),
					fieldWithPath("content.[].likeChecked").description("???????????? ????????? ?????? ??????????????? ??????")
						.type(JsonFieldType.BOOLEAN),
					fieldWithPath("content.[].imagePath").description("????????? ?????? ?????? ??????").type(JsonFieldType.STRING),
					fieldWithPath("pageable").description("????????? ?????? ??????"),
					fieldWithPath("last").description("????????? ??????????????? ??????"),
					fieldWithPath("totalPages").description("?????? ????????? ???"),
					fieldWithPath("totalElements").description("?????? ?????? ???"),
					fieldWithPath("first").description("????????? ??????????????? ??????"),
					fieldWithPath("size").description("??? ???????????? ????????? ?????? ???"),
					fieldWithPath("number").description("?????? ????????? ??????"),
					fieldWithPath("sort.empty").description("?????? ????????? ????????? ??????"),
					fieldWithPath("sort.sorted").description("???????????? ????????? ??????"),
					fieldWithPath("sort.unsorted").description("???????????? ?????? ????????? ??????"),
					fieldWithPath("numberOfElements").description("?????? ???????????? ????????? ?????? ???"),
					fieldWithPath("empty").description("?????? ???????????? ??????????????? ??????")
				)
			));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("?????? ????????? ??????")
	void getPost() throws Exception {
		var postId = 1L;
		var response = PostResponseDto.builder()
			.id(1L)
			.title("title of post")
			.userId(1L)
			.username("writer")
			.content("content of post")
			.createdAt(LocalDateTime.now())
			.modifiedAt(LocalDateTime.now())
			.likeCount(0)
			.isLikeChecked(false)
			.imagePath("image path")
			.build();

		when(postService.getPost(anyLong(), anyLong())).thenReturn(response);
		// when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.get("/posts/{postId}", postId)
				.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer accessToken"));
		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("post/get",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestHeaders(
					headerWithName("Authorization").description("???????????????")
				),
				pathParameters(
					parameterWithName("postId").description("????????? id")
				),
				responseFields(
					fieldWithPath("id").description("????????? id").type(JsonFieldType.NUMBER),
					fieldWithPath("title").description("????????? ??????").type(JsonFieldType.STRING),
					fieldWithPath("userId").description("????????? id").type(JsonFieldType.NUMBER),
					fieldWithPath("username").description("????????? ?????????").type(JsonFieldType.STRING),
					fieldWithPath("content").description("????????? ??????").type(JsonFieldType.STRING),
					fieldWithPath("createdAt").description("?????????").type(JsonFieldType.STRING),
					fieldWithPath("modifiedAt").description("?????????").type(JsonFieldType.STRING),
					fieldWithPath("likeCount").description("????????? ???").type(JsonFieldType.NUMBER),
					fieldWithPath("likeChecked").description("???????????? ????????? ?????? ??????????????? ??????")
						.type(JsonFieldType.BOOLEAN),
					fieldWithPath("imagePath").description("????????? ?????? ?????? ??????").type(JsonFieldType.STRING)
				)
			));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("????????? ??????")
	void createPost() throws Exception {
		// given
		var groupId = 1L;
		MockMultipartFile image = new MockMultipartFile("img", "image.png", "image/png",
			"<<png data>>".getBytes());
		MockMultipartFile request = new MockMultipartFile("requestDto", "",
			"application/json", ("{ \"title\": \"title of post\","
			+ "\"content\": \"content of post\" }").getBytes());
		// when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.multipart("/groups/{groupId}/post", groupId)
				.file(image).file(request)
				.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer accessToken")
				.accept(MediaType.APPLICATION_JSON));

		// then
		resultActions.andExpect(status().isCreated())
			.andDo(document("post/create",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestHeaders(
					headerWithName("Authorization").description("???????????????")
				),
				pathParameters(
					parameterWithName("groupId").description("?????? id")
				),
				requestPartFields("requestDto",
					fieldWithPath("title").description("????????? ??????").type(JsonFieldType.STRING),
					fieldWithPath("content").description("????????? ??????").type(JsonFieldType.STRING)
				),
				responseFields(
					fieldWithPath("data").description("???????????????")
				)
			));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("????????? ??????")
	void updatePost() throws Exception {
		// given
		var postId = 1L;
		MockMultipartFile image = new MockMultipartFile("img", "image.png", "image/png",
			"<<png data>>".getBytes());
		MockMultipartFile request = new MockMultipartFile("requestDto", "",
			"application/json", ("{ \"title\": \"title of post\","
			+ "\"content\": \"content of post\" }").getBytes());
		// when
		MockMultipartHttpServletRequestBuilder mockMultipartHttpServletRequestBuilder = (MockMultipartHttpServletRequestBuilder)multipart(
			HttpMethod.PUT, "/posts/{postId}", postId)
			.requestAttr(RestDocumentationGenerator.ATTRIBUTE_NAME_URL_TEMPLATE, "/posts/{postId}");

		ResultActions resultActions = mockMvc.perform(mockMultipartHttpServletRequestBuilder
			.file(image).file(request)
			.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer accessToken")
			.accept(MediaType.APPLICATION_JSON));

		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("post/update",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestHeaders(
					headerWithName("Authorization").description("???????????????")
				),
				pathParameters(
					parameterWithName("postId").description("????????? id")
				),
				requestPartFields("requestDto",
					fieldWithPath("title").description("????????? ??????").type(JsonFieldType.STRING),
					fieldWithPath("content").description("????????? ??????").type(JsonFieldType.STRING)
				),
				responseFields(
					fieldWithPath("data").description("???????????????")
				)
			));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("????????? ??????")
	void deletePost() throws Exception {
		//given
		doNothing().when(postService).deletePost(anyLong(), anyLong());
		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.delete("/posts/{postId}", 1L)
				.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer adminAccessToken"));
		//then
		resultActions.andExpect(status().isOk())
			.andDo(document("post/delete",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("postId").description("????????? id")
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
	@DisplayName("????????? ????????? ??????")
	void deletePostByAdmin() throws Exception {
		//given
		doNothing().when(postService).deletePostByAdmin(anyLong());
		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.delete("/admin/posts/{postId}", 1L)
				.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer adminAccessToken"));
		//then
		resultActions.andExpect(status().isOk())
			.andDo(document("post/delete-admin",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("postId").description("????????? id")
				),
				requestHeaders(
					headerWithName("Authorization").description("????????? ???????????????")
				),
				responseFields(
					fieldWithPath("data").description("???????????????")
				)
			));
	}
}