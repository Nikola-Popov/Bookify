package dev.popov.bookify.web.controllers;

import static dev.popov.bookify.commons.constants.RoleConstants.ADMIN;
import static dev.popov.bookify.web.controllers.constants.purchase.PurchasePathConstants.DELETE;
import static dev.popov.bookify.web.controllers.constants.purchase.PurchasePathConstants.PURCHASES;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

import dev.popov.bookify.domain.entity.Purchase;
import dev.popov.bookify.repository.PurchaseRepository;
import dev.popov.bookify.web.controllers.constants.purchase.PurchaseViewConstants;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PurchaseControllerTest {
	private static final String PURCHASES_OBJECT = "purchases";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private PurchaseRepository purchaseRepository;

	@Before
	public void setUp() {
		purchaseRepository.deleteAll();
	}

	@Test
	@WithMockUser(roles = ADMIN)
	public void testDelete() throws Exception {
		mockMvc.perform(delete(PURCHASES + DELETE + "/" + createPurchase().getId()))
				.andExpect(redirectedUrl(PURCHASES));
	}

	@Test
	@WithMockUser(roles = ADMIN)
	public void testPurchases() throws Exception {
		mockMvc.perform(get(PURCHASES)).andExpect(model().attributeExists(PURCHASES_OBJECT))
				.andExpect(view().name(PurchaseViewConstants.PURCHASES_ALL_PURCHASES));
	}

	private Purchase createPurchase() {
		final Purchase purchase = new Purchase();
		return purchaseRepository.saveAndFlush(purchase);
	}
}
