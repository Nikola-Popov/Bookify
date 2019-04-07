package dev.popov.bookify.domain.entity;

import static java.time.LocalDateTime.now;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "events")
public class Event extends BaseEntity {
	@Column(name = "name")
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(name = "event_type")
	private EventType eventType;

	@Column(name = "vouchers_count")
	private int vouchersCount;

	@Column(name = "description")
	private String description;

	@Column(name = "price")
	private BigDecimal price;

	@Column(name = "created_on")
	private LocalDateTime createdOn = now();

	@Column(name = "expires_on")
	private LocalDateTime expiresOn;
}
