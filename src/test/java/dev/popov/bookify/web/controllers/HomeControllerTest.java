package dev.popov.bookify.web.controllers;

import static dev.popov.bookify.web.controllers.constants.common.CommonPathConstants.HOME_PATH;
import static dev.popov.bookify.web.controllers.constants.common.CommonPathConstants.INDEX_PATH;
import static dev.popov.bookify.web.controllers.constants.common.CommonViewConstants.HOME;
import static dev.popov.bookify.web.controllers.constants.common.CommonViewConstants.INDEX;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class HomeControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testIndexReturnsCorrectView() throws Exception {
		mockMvc.perform(get(INDEX_PATH)).andExpect(view().name(INDEX));
	}

	@Test
	@WithMockUser
	public void testHomeReturnsCorrectView() throws Exception {
		mockMvc.perform(get(HOME_PATH)).andExpect(view().name(HOME));
	}
}
