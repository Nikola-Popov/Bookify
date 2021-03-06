package dev.popov.bookify.web.controllers;

import static dev.popov.bookify.web.controllers.constants.common.AuthorizationConstants.HAS_ADMIN_ROLE;
import static dev.popov.bookify.web.controllers.constants.common.AuthorizationConstants.IS_AUTHENTICATED;
import static dev.popov.bookify.web.controllers.constants.event.EventBindingConstants.EVENT_CREATE_BINDING_MODEL;
import static dev.popov.bookify.web.controllers.constants.event.EventPathConstants.BROWSE;
import static dev.popov.bookify.web.controllers.constants.event.EventPathConstants.CREATE;
import static dev.popov.bookify.web.controllers.constants.event.EventPathConstants.DELETE;
import static dev.popov.bookify.web.controllers.constants.event.EventPathConstants.EDIT;
import static dev.popov.bookify.web.controllers.constants.event.EventPathConstants.EVENTS;
import static dev.popov.bookify.web.controllers.constants.event.EventViewConstants.ALL_EVENTS;
import static dev.popov.bookify.web.controllers.constants.event.EventViewConstants.BROWSE_EVENTS;
import static dev.popov.bookify.web.controllers.constants.event.EventViewConstants.CREATE_EVENT;
import static dev.popov.bookify.web.controllers.constants.event.EventViewConstants.PREVIEW_EVENT;
import static java.util.stream.Collectors.toList;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import dev.popov.bookify.domain.model.binding.EventCreateBindingModel;
import dev.popov.bookify.domain.model.binding.EventEditBindingModel;
import dev.popov.bookify.domain.model.service.EventServiceModel;
import dev.popov.bookify.domain.model.view.EventViewModel;
import dev.popov.bookify.service.event.EventService;
import dev.popov.bookify.web.annotations.LogAction;
import dev.popov.bookify.web.annotations.PageTitle;

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

	@GetMapping(CREATE)
	@PreAuthorize(IS_AUTHENTICATED)
	@PageTitle("Create event")
	public ModelAndView create(
			@ModelAttribute(name = EVENT_CREATE_BINDING_MODEL) EventCreateBindingModel eventCreateBindingModel,
			ModelAndView modelAndView) {
		modelAndView.addObject(EVENT_CREATE_BINDING_MODEL, eventCreateBindingModel);

		return view(CREATE_EVENT, modelAndView);
	}

	@PostMapping(CREATE)
	@PreAuthorize(IS_AUTHENTICATED)
	@LogAction("Create event")
	public ModelAndView createConfirm(
			@ModelAttribute(name = EVENT_CREATE_BINDING_MODEL) EventCreateBindingModel eventCreateBindingModel) {
		eventService.create(modelMapper.map(eventCreateBindingModel, EventServiceModel.class));

		return redirect(EVENTS + BROWSE);
	}

	@GetMapping
	@PreAuthorize(IS_AUTHENTICATED)
	@PageTitle("All events")
	public ModelAndView fetchAll(ModelAndView modelAndView) {
		final List<EventViewModel> eventListViewModels = eventService.findAll().stream()
				.map(event -> modelMapper.map(event, EventViewModel.class)).collect(toList());
		modelAndView.addObject("eventListViewModels", eventListViewModels);

		return view(ALL_EVENTS, modelAndView);
	}

	@GetMapping(BROWSE)
	@PreAuthorize(IS_AUTHENTICATED)
	@PageTitle("Browse events")
	public ModelAndView browse() {
		return view(BROWSE_EVENTS);
	}

	@GetMapping(BROWSE + "/{id}")
	@PreAuthorize(IS_AUTHENTICATED)
	@PageTitle("Event")
	public ModelAndView browseById(@PathVariable(name = "id") String id, ModelAndView modelAndView) {
		modelAndView.addObject("eventViewModel", modelMapper.map(eventService.findById(id), EventViewModel.class));

		return view(PREVIEW_EVENT, modelAndView);
	}

	@PutMapping(EDIT + "/{id}")
	@PreAuthorize(HAS_ADMIN_ROLE)
	@LogAction("Edit event")
	public ModelAndView edit(@PathVariable(name = "id") String id,
			@ModelAttribute(name = "eventEditBindingModel") EventEditBindingModel eventEditBindingModel) {
		eventService.edit(id, modelMapper.map(eventEditBindingModel, EventServiceModel.class));

		return redirect(EVENTS);
	}

	@DeleteMapping(DELETE + "/{id}")
	@PreAuthorize(HAS_ADMIN_ROLE)
	@LogAction("Delete event")
	public ModelAndView delete(@PathVariable(name = "id") String id) {
		eventService.delete(id);

		return redirect(EVENTS);
	}
}
