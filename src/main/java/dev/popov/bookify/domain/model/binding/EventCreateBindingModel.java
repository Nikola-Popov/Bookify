package dev.popov.bookify.domain.model.binding;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EventCreateBindingModel {
	private String title;
	private String address;
	private EventTypeBindingModel eventType;
	private int vouchersCount;
	private String description;
	private BigDecimal price;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate expiresOn;
}
