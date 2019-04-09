package dev.popov.bookify.domain.model.view;

import java.math.BigDecimal;
import java.time.LocalDate;

import dev.popov.bookify.domain.entity.EventType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EventListViewModel extends BaseViewModel {
	private String title;
	private String address;
	private EventType eventType;
	private int vouchersCount;
	private String description;
	private BigDecimal price;
	private LocalDate createdOn;
	private LocalDate expiresOn;
}
