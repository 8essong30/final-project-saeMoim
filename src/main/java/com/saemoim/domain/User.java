package com.saemoim.domain;

import com.saemoim.domain.enums.UserRoleEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "users")
@Getter
@NoArgsConstructor
public class User extends TimeStamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false, unique = true)
	private String email;
	@Column(nullable = false)
	private String password;
	@Column(nullable = false, unique = true)
	private String username;
	// 회원가입 시, content 적는 게 없음. 논의 필요. - 해결
	@Column(nullable = false)
	private String content;
	@Column
	private int banCount;
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private UserRoleEnum role;

	public User(String email, String password, String username, UserRoleEnum role) {
		this.email = email;
		this.password = password;
		this.username = username;
		this.role = role;
		this.content = "안녕하세요. 잘 부탁드립니다.";
		this.banCount = 0;
	}

	public void plusBanCount() {
		this.banCount++;
	}

	public void updateStatus(UserRoleEnum role) {
		this.role = role;
	}
}
