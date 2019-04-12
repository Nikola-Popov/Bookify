package dev.popov.bookify.domain.model.view;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EventViewModel extends BaseViewModel {
	private String title;
	private String address;
	private EventTypeViewModel eventType;
	private int vouchersCount;
	private String description;
	private BigDecimal price;
	private LocalDate expiresOn;
}
