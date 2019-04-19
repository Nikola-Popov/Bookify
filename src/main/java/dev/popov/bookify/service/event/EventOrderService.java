package dev.popov.bookify.service.event;

import dev.popov.bookify.domain.model.service.EventOrderServiceModel;

public interface EventOrderService {
	EventOrderServiceModel create(EventOrderServiceModel eventOrderServiceModel);
}
