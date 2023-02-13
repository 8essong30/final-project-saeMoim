package com.saemoim.jwt;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saemoim.exception.ExceptionResponseDto;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws
		ServletException, IOException {

		String token = jwtUtil.resolveToken(request);

		if (token != null) {
			if (!jwtUtil.validateToken(token)) {
				jwtExceptionHandler(response, "Token Error", HttpStatus.UNAUTHORIZED);
				return;
			}
			Claims info = jwtUtil.getUserInfoFromToken(token);
			setAuthentication(info.getSubject());
		}
		filterChain.doFilter(request, response);
	}

	public void setAuthentication(String username) {
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		Authentication authentication = jwtUtil.createAuthentication(username);
		context.setAuthentication(authentication);

		SecurityContextHolder.setContext(context);
	}

	public void jwtExceptionHandler(HttpServletResponse response, String msg, HttpStatus statusCode) {
		response.setStatus(statusCode.value());
		response.setContentType("application/json");
		try {
			String json = new ObjectMapper().writeValueAsString(new ExceptionResponseDto(statusCode, msg));
			response.getWriter().write(json);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

}