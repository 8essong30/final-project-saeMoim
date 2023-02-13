package com.saemoim.domain;

import com.saemoim.domain.enums.BlacklistStatusEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class BlackList extends TimeStamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private BlacklistStatusEnum status;

	public Long getUserId() {
		return user.getId();
	}

	public String getUsername() {
		return user.getUsername();
	}

	public int getBanCount() {
		return user.getBanCount();
	}

	public void updateStatus(BlacklistStatusEnum status) {
		this.status = status;
	}

	public BlackList(User user, BlacklistStatusEnum status) {
		this.user = user;
		this.status = status;
	}
}
