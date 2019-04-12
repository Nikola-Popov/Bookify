package dev.popov.bookify.domain.model.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import dev.popov.bookify.domain.entity.EventType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EventServiceModel extends BaseServiceModel {
	@NotEmpty
	@NotNull
	private String title;
	private String address;
	private EventType eventType;
	@Min(value = 1, message = "You must specify atleast 1 available voucher")
	private int vouchersCount;
	private String description;
	private BigDecimal price;
	private LocalDate expiresOn;
}
