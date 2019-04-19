package dev.popov.bookify.domain.model.service;

import javax.validation.constraints.Min;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EventOrderServiceModel extends BaseServiceModel {
	@Min(value = 1, message = "Minimum is 1 item quantity")
	private int quantity;

	private EventServiceModel event;
}
