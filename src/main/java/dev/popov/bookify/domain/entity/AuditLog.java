package dev.popov.bookify.domain.entity;

import static java.time.LocalDateTime.now;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
public class AuditLog extends BaseEntity {
	@Column(name = "username", nullable = false)
	private String username;

	@Column(name = "action", nullable = false)
	private String action;

	@Column(name = "invoked_on", nullable = false, updatable = false)
	private LocalDateTime invokedOn = now();
}
