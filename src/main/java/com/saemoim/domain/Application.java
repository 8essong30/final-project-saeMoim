package com.saemoim.domain;

import com.saemoim.domain.enums.ApplicationStatusEnum;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Application extends TimeStamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "group_id")
	private Group group;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private ApplicationStatusEnum status;

	public Application(User user, Group group) {
		this.user = user;
		this.group = group;
		this.status = ApplicationStatusEnum.WAIT;
	}

	public Long getGroupId() {
		return this.group.getId();
	}

	public String getGroupName() {
		return this.group.getName();
	}

	public Long getUserId() {
		return this.user.getId();
	}

	public String getUsername() {
		return this.user.getUsername();
	}

	public boolean isRightUserWhoApllied(String username) {
		return this.getUsername().equals(username);
	}
}
