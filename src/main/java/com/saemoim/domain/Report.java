package com.saemoim.domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Report extends TimeStamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OnDelete(action = OnDeleteAction.CASCADE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "subject_id")
	private User subject;

	@Column(nullable = false)
	private String reporterName;
	@Column(nullable = false)
	private String content;

	public Long getSubjectId() {
		return this.subject.getId();
	}

	public String getSubjectUsername() {
		return this.subject.getUsername();
	}

	public Report(User subject, String reporterName, String content) {
		this.subject = subject;
		this.reporterName = reporterName;
		this.content = content;
	}
}
