package dev.popov.bookify.service.cart;

import org.springframework.stereotype.Component;

import dev.popov.bookify.domain.model.service.EventOrderServiceModel;
import dev.popov.bookify.domain.model.service.EventServiceModel;

@Component
public class EventOrderServiceFactory {
	public EventOrderServiceModel createEventOrderServiceModel(int quantity, EventServiceModel event) {
		return new EventOrderServiceModel(quantity, event);
	}
}
