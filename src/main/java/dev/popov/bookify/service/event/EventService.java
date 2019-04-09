package dev.popov.bookify.service.event;

import java.util.List;

import dev.popov.bookify.domain.model.service.EventServiceModel;

public interface EventService {
	List<EventServiceModel> findAll();

	void create(EventServiceModel eventServiceModel);
}
