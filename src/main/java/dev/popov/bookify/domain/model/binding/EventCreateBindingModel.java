package dev.popov.bookify.domain.model.binding;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EventCreateBindingModel {
	@NotEmpty
	@NotNull
	private String title;
	private String address;
	private EventTypeBindingModel eventType;
	@Min(value = 1, message = "You must specify atleast 1 available voucher")
	private int vouchersCount = 1;
	@NotEmpty
	@NotNull
	private String description;
	@Min(value = 0, message = "Invalid funds")
	private BigDecimal price;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate expiresOn;
}
