package com.saemoim.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.doNothing;
import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestPartFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.saemoim.annotation.WithCustomMockUser;
import com.saemoim.domain.User;
import com.saemoim.domain.enums.UserRoleEnum;
import com.saemoim.dto.request.EmailRequestDto;
import com.saemoim.dto.request.ProfileRequestDto;
import com.saemoim.dto.request.SignInRequestDto;
import com.saemoim.dto.request.SignUpRequestDto;
import com.saemoim.dto.request.UsernameRequestDto;
import com.saemoim.dto.request.WithdrawRequestDto;
import com.saemoim.dto.response.ProfileResponseDto;
import com.saemoim.dto.response.TokenResponseDto;
import com.saemoim.dto.response.UserResponseDto;
import com.saemoim.fileUpload.AWSS3Uploader;
import com.saemoim.jwt.JwtUtil;
import com.saemoim.oauth.CustomOAuth2UserService;
import com.saemoim.oauth.OAuth2AuthenticationSuccessHandler;
import com.saemoim.security.CustomAccessDeniedHandler;
import com.saemoim.security.CustomAuthenticationEntryPoint;
import com.saemoim.service.UserServiceImpl;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(controllers = UserController.class)
@MockBean(JpaMetamodelMappingContext.class)
class UserControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private UserServiceImpl userService;
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
	@DisplayName("?????? ??????")
	void signUp() throws Exception {
		//given
		SignUpRequestDto requestDto = SignUpRequestDto.builder()
			.username("nickName")
			.password("Password1!")
			.email("email@naver.com")
			.build();
		doNothing().when(userService).signUp(any(SignUpRequestDto.class));

		//when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.post("/sign-up")
			.contentType(MediaType.APPLICATION_JSON)
			.content(new Gson().toJson(requestDto)));

		//then
		resultActions.andExpect(status().isCreated())
			.andExpect(jsonPath("data").value("??????????????? ?????? ???????????????."))
			.andDo(document("user/sign-up",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("email").description("?????????").type(JsonFieldType.STRING),
					fieldWithPath("password").description("????????????").type(JsonFieldType.STRING),
					fieldWithPath("username").description("?????????").type(JsonFieldType.STRING)
				),
				responseFields(
					fieldWithPath("data").description("???????????????").type(JsonFieldType.STRING)
				)
			));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("????????? ?????? ??????")
	void checkEmailDuplication() throws Exception {
		//given
		EmailRequestDto requestDto = EmailRequestDto.builder()
			.email("email@naver.com")
			.build();
		doNothing().when(userService).checkEmailDuplication(any(EmailRequestDto.class));
		//when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.post("/sign-up/email")
			.contentType(MediaType.APPLICATION_JSON)
			.content(new Gson().toJson(requestDto)));
		//then
		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("data").value("????????? ?????? ????????? ?????? ???????????????."))
			.andDo(document("user/email-check",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("email").description("?????????").type(JsonFieldType.STRING)
				),
				responseFields(
					fieldWithPath("data").description("???????????????").type(JsonFieldType.STRING)
				)
			));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("????????? ?????? ??????")
	void checkUsernameDuplication() throws Exception {
		//given
		UsernameRequestDto requestDto = UsernameRequestDto.builder()
			.username("nickName")
			.build();
		doNothing().when(userService).checkUsernameDuplication(any(UsernameRequestDto.class));
		//when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.post("/sign-up/username")
			.contentType(MediaType.APPLICATION_JSON)
			.content(new Gson().toJson(requestDto)));
		//then
		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("data").value("?????? ?????? ????????? ?????? ???????????????."))
			.andDo(document("user/username-check",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("username").description("?????????").type(JsonFieldType.STRING)
				),
				responseFields(
					fieldWithPath("data").description("???????????????").type(JsonFieldType.STRING)
				)
			));
	}

	@Test
	@DisplayName("?????????")
	@WithCustomMockUser
	void signIn() throws Exception {
		//given
		TokenResponseDto responseDto = mock(TokenResponseDto.class);
		SignInRequestDto requestDto = SignInRequestDto.builder()
			.email("email@naver.com")
			.password("Pass1234!")
			.build();

		when(responseDto.getAccessToken()).thenReturn("Bearer accessToken");
		when(responseDto.getRefreshToken()).thenReturn("Bearer refreshToken");
		when(userService.signIn(any(SignInRequestDto.class))).thenReturn(responseDto);
		//when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.post("/sign-in")
			.contentType(MediaType.APPLICATION_JSON)
			.content(new Gson().toJson(requestDto)));
		//then
		resultActions.andExpect(status().isOk())
			.andExpect(header().string("Authorization", "Bearer accessToken"))
			.andExpect(header().string("RefreshToken", "Bearer refreshToken"))
			.andExpect(jsonPath("data").value("???????????? ?????? ???????????????."))
			.andDo(document("user/login",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("email").description("?????????").type(JsonFieldType.STRING),
					fieldWithPath("password").description("????????????").type(JsonFieldType.STRING)
				),
				responseHeaders(
					headerWithName("Authorization").description("???????????????"),
					headerWithName("RefreshToken").description("??????????????????")
				),
				responseFields(
					fieldWithPath("data").description("???????????????").type(JsonFieldType.STRING)
				)
			));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("????????????")
	void logout() throws Exception {
		//given
		doNothing().when(userService).logout(anyString());

		//when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.post("/log-out")
			.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer accessToken")
			.header(JwtUtil.REFRESH_TOKEN_HEADER, "Bearer refreshToken")
			.contentType(MediaType.APPLICATION_JSON));

		//then
		resultActions.andExpect(status().isOk())
			.andExpect(header().string("Authorization", ""))
			.andExpect(jsonPath("data").value("??????????????? ?????? ???????????????."))
			.andDo(document("user/logout",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestHeaders(
					headerWithName("Authorization").description("???????????????"),
					headerWithName("RefreshToken").description("??????????????????")
				),
				responseFields(
					fieldWithPath("data").description("???????????????").type(JsonFieldType.STRING)
				)
			));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("?????? ??????")
	void withdraw() throws Exception {
		//given
		WithdrawRequestDto requestDto = WithdrawRequestDto.builder()
			.password("Pass1234!")
			.build();

		doNothing().when(userService).withdraw(requestDto, 1L, "Bearer refreshToken");
		//when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.delete("/withdrawal")
			.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer accessToken")
			.header(JwtUtil.REFRESH_TOKEN_HEADER, "Bearer refreshToken")
			.contentType(MediaType.APPLICATION_JSON)
			.content(new Gson().toJson(requestDto)));

		//then
		resultActions.andExpect(status().isOk())
			.andExpect(header().string("Authorization", ""))
			.andExpect(jsonPath("data").value("??????????????? ?????? ???????????????."))
			.andDo(document("user/withdrawal",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestHeaders(
					headerWithName("Authorization").description("???????????????"),
					headerWithName("RefreshToken").description("??????????????????")
				),
				requestFields(
					fieldWithPath("password").description("????????????").type(JsonFieldType.STRING)
				),
				responseFields(
					fieldWithPath("data").description("???????????????").type(JsonFieldType.STRING)
				)
			));
	}

	@Test
	@WithCustomMockUser(role = UserRoleEnum.ADMIN)
	@DisplayName("?????? ?????? ??????")
	void getAllUsers() throws Exception {
		//given
		List<UserResponseDto> list = new ArrayList<>();
		UserResponseDto responseDto = UserResponseDto.builder()
			.id(1L)
			.banCount(0)
			.email("email@naver.com")
			.username("nickName")
			.createdAt(LocalDateTime.now())
			.build();

		list.add(responseDto);

		when(userService.getAllUsers()).thenReturn(list);
		//when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get("/admin/user")
			.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer adminAccessToken"));
		//then
		resultActions.andExpect(status().isOk())
			.andDo(document("user/getAll",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestHeaders(
					headerWithName("Authorization").description("????????????????????????")
				),
				responseFields(
					subsectionWithPath("data").description("????????????"),
					fieldWithPath("data.[].id").description("?????? id").type(JsonFieldType.NUMBER),
					fieldWithPath("data.[].email").description("?????? ?????????").type(JsonFieldType.STRING),
					fieldWithPath("data.[].username").description("?????? ?????????").type(JsonFieldType.STRING),
					fieldWithPath("data.[].banCount").description("???????????? ??????").type(JsonFieldType.NUMBER),
					fieldWithPath("data.[].createdAt").description("????????????").type(JsonFieldType.STRING)
				)
			));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("?????? ????????? ??????")
	void getProfile() throws Exception {
		//given
		User user = User.builder()
			.id(1L)
			.banCount(0)
			.content("?????????????????????")
			.email("email@naver.com")
			.password("Pass1234!")
			.role(UserRoleEnum.USER)
			.username("?????????")
			.imagePath("imgPath")
			.build();

		ProfileResponseDto response = new ProfileResponseDto(user);

		when(userService.getProfile(anyLong())).thenReturn(response);
		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.get("/profile/users/{userId}", 1L)
				.header("Authorization", "Bearer accessToken"));
		//then
		resultActions.andExpect(status().isOk())
			.andDo(document("user/profile",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("userId").description("?????? id")
				),
				requestHeaders(
					headerWithName("Authorization").description("???????????????")
				),
				responseFields(
					fieldWithPath("id").description("?????? id").type(JsonFieldType.NUMBER),
					fieldWithPath("username").description("?????? ?????????").type(JsonFieldType.STRING),
					fieldWithPath("content").description("?????????").type(JsonFieldType.STRING),
					fieldWithPath("imagePath").description("????????? ????????????").type(JsonFieldType.STRING)
				)
			));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("??? ?????? ??????")
	void getMyProfile() throws Exception {
		// given
		User user = User.builder()
			.id(1L)
			.banCount(0)
			.content("?????????????????????")
			.email("email@naver.com")
			.password("Pass1234!")
			.role(UserRoleEnum.USER)
			.username("?????????")
			.imagePath("imgPath")
			.build();
		ProfileResponseDto response = new ProfileResponseDto(user);

		when(userService.getMyProfile(anyLong())).thenReturn(response);

		// when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get("/profile")
			.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer accessToken"));

		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("user/myProfile",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestHeaders(
					headerWithName("Authorization").description("???????????????")
				),
				responseFields(
					fieldWithPath("id").description("?????? id").type(JsonFieldType.NUMBER),
					fieldWithPath("username").description("?????? ?????????").type(JsonFieldType.STRING),
					fieldWithPath("content").description("?????????").type(JsonFieldType.STRING),
					fieldWithPath("imagePath").description("????????? ????????????").type(JsonFieldType.STRING)
				)
			));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("?????????????????????")
	void getUserId() throws Exception {
		// when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get("/user")
			.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer accessToken"));

		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("user/user-id",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestHeaders(
					headerWithName("Authorization").description("???????????????")
				),
				responseFields(
					fieldWithPath("data").description("?????? id").type(JsonFieldType.NUMBER)
				)
			));
	}

	@Test
	@WithCustomMockUser
	@DisplayName("??? ?????? ??????")
	void updateProfile() throws Exception {
		// given
		MockMultipartFile image = new MockMultipartFile("img", "image.png", "image/png",
			"<<png data>>".getBytes());
		MockMultipartFile request = new MockMultipartFile("requestDto", "",
			"application/json", "{ \"content\": \"1.0\"}".getBytes());
		User user = User.builder()
			.id(1L)
			.banCount(0)
			.content("?????????????????????")
			.email("email@naver.com")
			.password("Pass1234!")
			.role(UserRoleEnum.USER)
			.username("?????????")
			.imagePath("imgPath")
			.build();
		ProfileResponseDto responseDto = new ProfileResponseDto(user);
		when(userService.updateProfile(anyLong(), any(ProfileRequestDto.class), any(MultipartFile.class))).thenReturn(
			responseDto);
		// when
		MockMultipartHttpServletRequestBuilder mockMultipartHttpServletRequestBuilder = (MockMultipartHttpServletRequestBuilder)multipart(
			HttpMethod.PUT, "/profile")
			.requestAttr(RestDocumentationGenerator.ATTRIBUTE_NAME_URL_TEMPLATE, "/profile");
		ResultActions resultActions = mockMvc.perform(mockMultipartHttpServletRequestBuilder
			.file(image).file(request)
			.header(JwtUtil.AUTHORIZATION_HEADER, "Bearer accessToken")
			.accept(MediaType.APPLICATION_JSON));

		// then
		resultActions.andExpect(status().isOk())
			.andDo(document("user/update-profile",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestPartFields("requestDto",
					fieldWithPath("content").description("?????????").type(JsonFieldType.STRING)
				),
				requestHeaders(
					headerWithName("Authorization").description("???????????????")
				),
				responseFields(
					fieldWithPath("id").description("?????? id").type(JsonFieldType.NUMBER),
					fieldWithPath("username").description("?????? ?????????").type(JsonFieldType.STRING),
					fieldWithPath("content").description("?????????").type(JsonFieldType.STRING),
					fieldWithPath("imagePath").description("????????? ????????????").type(JsonFieldType.STRING)
				)
			));

	}

	@Test
	@WithCustomMockUser
	@DisplayName("???????????? ?????? ?????????")
	void reissueSuccess() throws Exception {
		// given
		String accessToken = "Bearer accessToken";
		String refreshToken = "Bearer refreshToken";
		TokenResponseDto tokenResponseDto = new TokenResponseDto(accessToken, refreshToken);
		when(userService.reissueToken(anyString(), anyString())).thenReturn(tokenResponseDto);
		// when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.post("/reissue")
			.header(JwtUtil.AUTHORIZATION_HEADER, accessToken)
			.header(JwtUtil.REFRESH_TOKEN_HEADER, refreshToken));

		// then
		resultActions.andExpect(status().isOk())
			.andExpect(header().string(JwtUtil.AUTHORIZATION_HEADER, accessToken))
			.andExpect(header().string(JwtUtil.REFRESH_TOKEN_HEADER, refreshToken))
			.andDo(document("user/reissue",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestHeaders(
					headerWithName("Authorization").description("???????????????"),
					headerWithName("RefreshToken").description("??????????????????")
				),
				responseHeaders(
					headerWithName("Authorization").description("???????????????"),
					headerWithName("RefreshToken").description("??????????????????")
				),
				responseFields(
					fieldWithPath("data").description("???????????????").type(JsonFieldType.STRING)
				)
			));
	}

}