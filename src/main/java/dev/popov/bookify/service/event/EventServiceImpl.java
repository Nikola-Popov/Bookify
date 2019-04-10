package dev.popov.bookify.service.event;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.popov.bookify.domain.entity.Event;
import dev.popov.bookify.domain.entity.EventType;
import dev.popov.bookify.domain.model.service.EventServiceModel;
import dev.popov.bookify.domain.model.service.EventTypeServiceModel;
import dev.popov.bookify.repository.EventRepository;

@Service
public class EventServiceImpl implements EventService {
	private final EventRepository eventRepository;
	private final ModelMapper modelMapper;

	@Autowired
	public EventServiceImpl(EventRepository eventRepository, ModelMapper modelMapper) {
		this.eventRepository = eventRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public List<EventServiceModel> findAll() {
		return eventRepository.findAll().stream().map(event -> modelMapper.map(event, EventServiceModel.class))
				.collect(toList());
	}

	@Override
	public void create(EventServiceModel eventServiceModel) {
		eventRepository.saveAndFlush(modelMapper.map(eventServiceModel, Event.class));
	}

	@Override
	public List<EventServiceModel> findAllByEventType(EventTypeServiceModel eventTypeServiceModel) {
		if (eventTypeServiceModel.equals(EventTypeServiceModel.ALL)) {
			return findAll();
		}

		return eventRepository.findAllByEventType(modelMapper.map(eventTypeServiceModel, EventType.class)).stream()
				.map(event -> modelMapper.map(event, EventServiceModel.class)).collect(toList());
	}
}
