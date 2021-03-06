package dev.popov.bookify.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "events")
public class Event extends BaseEntity {
	@Column(name = "title", nullable = false)
	@NotEmpty
	private String title;

	@Column(name = "address")
	private String address;

	@Enumerated(EnumType.STRING)
	@Column(name = "event_type")
	private EventType eventType;

	@Column(name = "vouchers_count")
	private int vouchersCount = 1;

	@Column(name = "description")
	@NotEmpty
	private String description;

	@Column(name = "price")
	@Min(value = 0, message = "Invalid funds")
	private BigDecimal price = BigDecimal.ZERO;

	@Column(name = "expires_on")
	private LocalDate expiresOn;
}
