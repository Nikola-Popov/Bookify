package dev.popov.bookify.web.controllers;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import dev.popov.bookify.domain.model.view.EventListViewModel;
import dev.popov.bookify.service.interfaces.EventService;

@Controller
@RequestMapping("/events")
public class EventController extends BaseController {
	private final EventService eventService;
	private final ModelMapper modelMapper;

	@Autowired
	public EventController(EventService eventService, ModelMapper modelMapper) {
		this.eventService = eventService;
		this.modelMapper = modelMapper;
	}

	@GetMapping
	public ModelAndView all(ModelAndView modelAndView) {
		final List<EventListViewModel> eventListViewModels = eventService.findAll().stream()
				.map(event -> modelMapper.map(event, EventListViewModel.class)).collect(toList());

		modelAndView.addObject("eventListViewModels", eventListViewModels);

		return view("all_events", modelAndView);
	}
}