package dev.popov.bookify.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.Min;

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
	private String title;

	@Column(name = "address")
	private String address;

	@Enumerated(EnumType.STRING)
	@Column(name = "event_type")
	private EventType eventType;

	@Column(name = "vouchers_count")
	@Min(value = 1, message = "You must specify atleast 1 available voucher")
	private int vouchersCount = 1;

	@Column(name = "description")
	private String description;

	@Column(name = "price")
	private BigDecimal price;

	@Column(name = "expires_on")
	private LocalDate expiresOn;
}
