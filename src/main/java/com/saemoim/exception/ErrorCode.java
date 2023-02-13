package com.saemoim.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
	INVALID_AUTH_TOKEN("권한 정보가 없는 토큰입니다"),
	INVALID_TOKEN("토큰이 유효하지 않습니다."),
	DUPLICATED_USERNAME("중복된 username 입니다"),
	DUPLICATED_EMAIL("중복된 email 입니다"),
	NOT_FOUND_USER("회원을 찾을 수 없습니다."),
	INVALID_PASSWORD("비밀번호가 틀렸습니다."),

	DUPLICATED_CATEGORY("중복된 카테고리 입니다."),
	NOT_EMPTY_CATEGORY("비어있지 않은 카테고리는 삭제할 수 없습니다."),
	NOT_EXIST_CATEGORY("카테고리가 존재하지 않습니다."),
	NOT_PARENT_CATEGORY("기본 카테고리 밑에 생성할 수 있습니다."),

	DUPLICATED_BLACKLIST("이미 블랙리스트에 등록되어 있습니다."),
	NOT_EXIST_BLACKLIST("해당 블랙리스트가 존재하지 않습니다."),

	NOT_READABLE_JSON("올바르지 않은 JSON 형식입니다."),
	NOT_SUPPORTED_HTTP_MEDIA_TYPE("지원하지 않는 Content-Type 입니다."),
	NOT_ACCEPTABLE_HTTP_MEDIA_TYPE("지원하지 않는 Accept 입니다."),

	INTERNAL_SERVER_ERROR("서버 오류");

	private final String message;
}
