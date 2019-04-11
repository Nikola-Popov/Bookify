package dev.popov.bookify.web.controllers;

import static dev.popov.bookify.web.controllers.constants.view.HomeViewConstants.HOME;
import static dev.popov.bookify.web.controllers.constants.view.HomeViewConstants.INDEX;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HomeControllerTest {
	private HomeController homeController;

	@Before
	public void setUp() {
		homeController = new HomeController();
	}

	@Test
	public void testIndexReturnsCorrectView() {
		assertThat(homeController.index().getViewName(), equalTo(INDEX));
	}

	@Test
	public void testHomeReturnsCorrectView() {
		assertThat(homeController.home().getViewName(), equalTo(HOME));
	}
}
