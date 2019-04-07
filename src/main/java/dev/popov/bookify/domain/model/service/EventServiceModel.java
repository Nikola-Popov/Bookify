package dev.popov.bookify.domain.model.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
	private String name;
	private EventType eventType;
	private int vouchersCount;
	private String description;
	private BigDecimal price;
	private LocalDateTime createdOn;
	private LocalDateTime expiresOn;
}
