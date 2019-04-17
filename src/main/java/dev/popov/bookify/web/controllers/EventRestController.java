package dev.popov.bookify.web.controllers;

import static dev.popov.bookify.web.controllers.constants.event.EventPathConstants.FILTER;
import static java.util.stream.Collectors.toList;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import dev.popov.bookify.domain.model.service.EventTypeServiceModel;
import dev.popov.bookify.domain.model.view.EventViewModel;
import dev.popov.bookify.service.event.EventService;

@RestController("/events")
public class EventRestController {
	private final EventService eventService;
	private final ModelMapper modelMapper;

	@Autowired
	public EventRestController(EventService eventService, ModelMapper modelMapper) {
		this.eventService = eventService;
		this.modelMapper = modelMapper;
	}

	@GetMapping(FILTER)
	@ResponseBody
	public List<EventViewModel> fetchApplyingFilter(@RequestParam(name = "type") String type) {
		return eventService.findAllByEventType(EventTypeServiceModel.valueOf(type)).stream()
				.map(event -> modelMapper.map(event, EventViewModel.class)).collect(toList());
	}
}
