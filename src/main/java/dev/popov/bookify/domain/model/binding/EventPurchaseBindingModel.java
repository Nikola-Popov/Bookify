package dev.popov.bookify.domain.model.binding;

import java.math.BigDecimal;
import java.time.LocalDate;

import dev.popov.bookify.domain.model.view.EventTypeViewModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EventPurchaseBindingModel {
	private String title;
	private String address;
	private EventTypeViewModel eventType;
	private int vouchersCount;
	private String description;
	private BigDecimal price;
	private LocalDate expiresOn;
}
