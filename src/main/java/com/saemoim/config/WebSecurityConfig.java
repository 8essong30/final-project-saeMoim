package com.saemoim.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.saemoim.jwt.JwtAuthFilter;
import com.saemoim.jwt.JwtUtil;
import com.saemoim.security.CustomAccessDeniedHandler;
import com.saemoim.security.CustomAuthenticationEntryPoint;
import com.saemoim.security.UserDetailsServiceImpl;

import lombok.RequiredArgsConstructor;

// @EnableMethodSecurity	// @Secured 어노테이션 활성화
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

	private final JwtUtil jwtUtil;
	private final UserDetailsServiceImpl userDetailsService;
	private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
	private final CustomAccessDeniedHandler customAccessDeniedHandler;

	// 비밀번호 단방향 암호화 인코더
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer(){
		// static resources ( css, js, images 등 ) 자원에 대한 접근 허용
		return (web -> web.ignoring()
			.requestMatchers(PathRequest.toStaticResources().atCommonLocations()));
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// jwt 방식에선 사용하지 않아도 됨.
		http.csrf().disable();
		// Session 방식 사용하지 않고 JWT 방식 사용하기 위한 설정
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		// 내용 추가 필요함. 로그인 페이지, 회원가입 페이지 등
		http.authorizeHttpRequests()
			.requestMatchers("/email-verification").permitAll()
			.requestMatchers("/sign-up").permitAll()
			.requestMatchers("/sign-in").permitAll()
			.requestMatchers("/admin/**").hasAnyRole("ADMIN")
			.and().addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
			// .anyRequest().authenticated();	// 모든 요청에 대해 인증. 당장 사용하지 않으므로 주석 처리

		http.formLogin().disable();
			// .loginPage("/로그인form url").permitAll();

		// http.addFilterBefore(new CustomSecurityFilter(userDetailsService, passwordEncoder()), UsernamePasswordAuthenticationFilter.class);

		// 접근 제한 페이지 이동
		// http.exceptionHandling().accessDeniedPage("/api/user/forbidden");
		http.exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint);
		http.exceptionHandling().accessDeniedHandler(customAccessDeniedHandler);

		return http.build();	// 상기 설정들을 빌드하여 리턴
	}
}