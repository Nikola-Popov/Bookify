package dev.popov.bookify.web.controllers;

import static dev.popov.bookify.commons.constants.AuthorityConstants.ADMIN;
import static dev.popov.bookify.web.controllers.constants.event.EventBindingConstants.EVENT_CREATE_BINDING_MODEL;
import static dev.popov.bookify.web.controllers.constants.event.EventPathConstants.BROWSE;
import static dev.popov.bookify.web.controllers.constants.event.EventPathConstants.CREATE;
import static dev.popov.bookify.web.controllers.constants.event.EventPathConstants.EDIT;
import static dev.popov.bookify.web.controllers.constants.event.EventPathConstants.EVENTS;
import static dev.popov.bookify.web.controllers.constants.event.EventViewConstants.ALL_EVENTS;
import static dev.popov.bookify.web.controllers.constants.event.EventViewConstants.BROWSE_EVENTS;
import static dev.popov.bookify.web.controllers.constants.event.EventViewConstants.CREATE_EVENT;
import static dev.popov.bookify.web.controllers.constants.event.EventViewConstants.PREVIEW_EVENT;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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

import dev.popov.bookify.domain.model.service.EventServiceModel;
import dev.popov.bookify.repository.EventRepository;
import dev.popov.bookify.service.event.EventService;
import dev.popov.bookify.web.controllers.constants.event.EventPathConstants;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class EventControllerTest {
	private static final String EDIT_TITLE = "editTitle";
	private static final String EDIT_DESCRIPTION = "editDescription";
	private static final int COUNT = 3;
	private static final String VOUCHERS_COUNT = "vouchersCount";
	private static final String TITLE = "title";
	private static final String DESCRIPTION = "description";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private EventService eventService;

	@Autowired
	private EventRepository eventRepository;

	@Before
	public void setUp() {
		eventRepository.deleteAll();
	}

	@Test
	@WithMockUser
	public void testCreate() throws Exception {
		mockMvc.perform(get(EVENTS + CREATE)).andExpect(view().name(CREATE_EVENT))
				.andExpect(model().attributeExists(EVENT_CREATE_BINDING_MODEL));
	}

	@Test
	@WithMockUser
	public void testCreateConfirm() throws Exception {
		mockMvc.perform(post(EVENTS + CREATE).param(TITLE, TITLE).param(VOUCHERS_COUNT, String.valueOf(COUNT))
				.param(DESCRIPTION, DESCRIPTION)).andExpect(redirectedUrl(EVENTS + BROWSE));

		assertEventServiceModelFields(TITLE, COUNT, DESCRIPTION);
	}

	@Test
	@WithMockUser
	public void testFetchAll() throws Exception {
		mockMvc.perform(get(EVENTS)).andExpect(model().attributeExists("eventListViewModels"))
				.andExpect(view().name(ALL_EVENTS));
	}

	@Test
	@WithMockUser
	public void testBrowse() throws Exception {
		mockMvc.perform(get(EVENTS + BROWSE)).andExpect(view().name(BROWSE_EVENTS));
	}

	@Test
	@WithMockUser
	public void testBrowseById() throws Exception {
		mockMvc.perform(get(EVENTS + BROWSE + "/" + createEventServiceModel().getId()))
				.andExpect(model().attributeExists("eventViewModel")).andExpect(view().name(PREVIEW_EVENT));
	}

	@Test
	@WithMockUser(roles = ADMIN)
	public void testEdit() throws Exception {
		mockMvc.perform(put(EVENTS + EDIT + "/" + createEventServiceModel().getId()).param(TITLE, EDIT_TITLE)
				.param(VOUCHERS_COUNT, String.valueOf(COUNT)).param(DESCRIPTION, EDIT_DESCRIPTION))
				.andExpect(redirectedUrl(EVENTS));

		assertEventServiceModelFields(EDIT_TITLE, COUNT, EDIT_DESCRIPTION);
	}

	@Test
	@WithMockUser(roles = ADMIN)
	public void testDelete() throws Exception {
		mockMvc.perform(delete(EVENTS + EventPathConstants.DELETE + "/" + createEventServiceModel().getId()))
				.andExpect(redirectedUrl(EVENTS));

		assertThat(eventRepository.findAll().isEmpty(), equalTo(true));
	}

	private void assertEventServiceModelFields(String title, int vouchersCount, String description) {
		final EventServiceModel eventServiceModel = eventService.findAll().get(0);
		assertThat(eventServiceModel.getTitle(), equalTo(title));
		assertThat(eventServiceModel.getVouchersCount(), equalTo(vouchersCount));
		assertThat(eventServiceModel.getDescription(), equalTo(description));
	}

	private EventServiceModel createEventServiceModel() {
		final EventServiceModel eventServiceModel = new EventServiceModel();
		eventServiceModel.setTitle(TITLE);
		eventServiceModel.setDescription(DESCRIPTION);
		eventServiceModel.setVouchersCount(COUNT);
		return eventService.create(eventServiceModel);
	}
}
