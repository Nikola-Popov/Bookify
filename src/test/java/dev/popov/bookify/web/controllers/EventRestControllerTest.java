package dev.popov.bookify.web.controllers;

import static dev.popov.bookify.domain.entity.EventType.FOOD;
import static dev.popov.bookify.web.controllers.constants.event.EventPathConstants.EVENTS;
import static dev.popov.bookify.web.controllers.constants.event.EventPathConstants.FILTER;
import static java.util.Arrays.asList;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.google.gson.Gson;

import dev.popov.bookify.domain.model.service.EventServiceModel;
import dev.popov.bookify.repository.EventRepository;
import dev.popov.bookify.service.event.EventService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class EventRestControllerTest {
	private static final int COUNT = 3;
	private static final String TITLE = "title";
	private static final String DESCRIPTION = "description";

	@Autowired
	private EventService eventService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private EventRepository eventRepository;

	@Before
	public void setUp() {
		eventRepository.deleteAll();
	}

	@Test
	@WithMockUser
	public void testFetchApplyingFilterNotMatchingType() throws Exception {
		mockMvc.perform(get(EVENTS + FILTER).param("type", "FOOD")).andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8)).andExpect(content().json("[]"));
	}

	@Test
	@WithMockUser
	public void testFetchApplyingFilter() throws Exception {
		final EventServiceModel eventServiceModel = createEventServiceModel();

		mockMvc.perform(get(EVENTS + FILTER).param("type", FOOD.toString())).andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(content().json(new Gson().toJson(asList(eventServiceModel))));
	}

	private EventServiceModel createEventServiceModel() {
		final EventServiceModel eventServiceModel = new EventServiceModel();
		eventServiceModel.setTitle(TITLE);
		eventServiceModel.setDescription(DESCRIPTION);
		eventServiceModel.setVouchersCount(COUNT);
		eventServiceModel.setEventType(FOOD);

		return eventService.create(eventServiceModel);
	}
}
