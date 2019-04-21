package dev.popov.bookify.service.event;

import java.util.List;

import dev.popov.bookify.domain.model.service.EventServiceModel;
import dev.popov.bookify.domain.model.service.EventTypeServiceModel;

public interface EventService {
	List<EventServiceModel> findAll();

	EventServiceModel findById(String id);

	EventServiceModel create(EventServiceModel eventServiceModel);

	List<EventServiceModel> findAllByEventType(EventTypeServiceModel eventTypeServiceModel);

	void delete(String id);

	void edit(String id, EventServiceModel eventServiceModel);
}
