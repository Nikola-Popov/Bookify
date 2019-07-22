package dev.popov.bookify.web.controllers;

import static dev.popov.bookify.commons.constants.AuthorityConstants.ADMIN;
import static dev.popov.bookify.web.controllers.constants.user.UserPathConstants.DELETE;
import static dev.popov.bookify.web.controllers.constants.user.UserPathConstants.EDIT;
import static dev.popov.bookify.web.controllers.constants.user.UserPathConstants.LOGIN_PATH;
import static dev.popov.bookify.web.controllers.constants.user.UserPathConstants.PROFILE;
import static dev.popov.bookify.web.controllers.constants.user.UserPathConstants.REGISTER_PATH;
import static dev.popov.bookify.web.controllers.constants.user.UserPathConstants.SETTINGS;
import static dev.popov.bookify.web.controllers.constants.user.UserPathConstants.USERS;
import static dev.popov.bookify.web.controllers.constants.user.UserViewConstants.ALL_USERS;
import static dev.popov.bookify.web.controllers.constants.user.UserViewConstants.LOGIN;
import static dev.popov.bookify.web.controllers.constants.user.UserViewConstants.REGISTER;
import static dev.popov.bookify.web.controllers.constants.user.UserViewConstants.USER_SETTINGS;
import static dev.popov.bookify.web.controllers.constants.user.UserViewConstants.USER_SETTINGS_CHANGE_PASSWORD;
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
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import dev.popov.bookify.domain.model.binding.ContactRegisterBindingModel;
import dev.popov.bookify.domain.model.binding.UserRegisterBindingModel;
import dev.popov.bookify.domain.model.service.UserServiceModel;
import dev.popov.bookify.repository.UserRepository;
import dev.popov.bookify.service.user.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserControllerTest {
	private static final String CONFIRM_NEW_PASSWORD = "confirmNewPassword";
	private static final String NEW_PASSWORD = "newPassword";
	private static final String NEW_USERNAME = "newUsername";
	private static final String ADMIN_EMAIL = "admin@email.com";
	private static final String USER_EMAIL = "user@email.com";
	private static final String EMAIL = "contact.email";
	private static final String PASSWORD = "password";
	private static final String ADMIN_USERNAME = "adminUsername";
	private static final String USERNAME = "username";
	private static final String CONFIRM_PASSWORD = "confirmPassword";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Before
	public void setUp() {
		userRepository.deleteAll();
		registerUser(ADMIN_USERNAME, ADMIN_EMAIL);
	}

	@Test
	@WithMockUser(roles = ADMIN)
	public void testFetchAll() throws Exception {
		mockMvc.perform(get(USERS)).andExpect(model().attributeExists("userListViewModels"))
				.andExpect(view().name(ALL_USERS));
	}

	@Test
	public void testRegister() throws Exception {
		mockMvc.perform(get(USERS + REGISTER_PATH)).andExpect(model().attributeExists("userRegisterBindingModel"))
				.andExpect(view().name(REGISTER));
	}

	@Test
	public void testRegisterConfirm() throws Exception {
		userRepository.deleteAll();

		mockMvc.perform(post(USERS + REGISTER_PATH).param(USERNAME, USERNAME).param(PASSWORD, PASSWORD)
				.param(EMAIL, USER_EMAIL).param(CONFIRM_PASSWORD, PASSWORD))
				.andExpect(redirectedUrl(USERS + LOGIN_PATH));

		final UserServiceModel userServiceModel = userService.findAll().get(0);
		assertThat(userServiceModel.getUsername(), equalTo(USERNAME));
		assertThat(userServiceModel.getContact().getEmail(), equalTo(USER_EMAIL));
		assertThat(passwordEncoder.matches(PASSWORD, userServiceModel.getPassword()), equalTo(true));
	}

	@Test
	public void testLogin() throws Exception {
		mockMvc.perform(get(USERS + LOGIN_PATH)).andExpect(view().name(LOGIN));
	}

	@Test
	@WithMockUser(roles = ADMIN)
	public void testEdit() throws Exception {
		mockMvc.perform(put(USERS + EDIT + "/" + registerUser(USERNAME, USER_EMAIL).getId()).param(USERNAME, USERNAME)
				.param("contact.email", USER_EMAIL)).andExpect(redirectedUrl(USERS));
	}

	@Test
	@WithMockUser(roles = ADMIN)
	public void testDelete() throws Exception {
		mockMvc.perform(delete(USERS + DELETE + "/" + registerUser(USERNAME, USER_EMAIL).getId()))
				.andExpect(redirectedUrl(USERS));
	}

	@Test
	@WithMockUser(username = USERNAME)
	public void testSettings() throws Exception {
		registerUser(USERNAME, USER_EMAIL);

		mockMvc.perform(get(USERS + PROFILE + SETTINGS)).andExpect(model().attributeExists("userSettingsViewModel"))
				.andExpect(view().name(USER_SETTINGS));
	}

	@Test
	@WithMockUser
	public void testSettingsConfirm() throws Exception {
		mockMvc.perform(put(USERS + PROFILE + SETTINGS + "/" + registerUser(USERNAME, USER_EMAIL).getId())
				.param(USERNAME, NEW_USERNAME)).andExpect(redirectedUrl(USERS + PROFILE + SETTINGS));
	}

	@Test
	@WithMockUser(username = USERNAME)
	@Ignore
	public void testChangePassword() throws Exception {
		registerUser(USERNAME, USER_EMAIL);

		mockMvc.perform(get(USERS + PROFILE + SETTINGS + PASSWORD))
				.andExpect(model().attributeExists("userPasswordChangeBindingModel"))
				.andExpect(view().name(USER_SETTINGS_CHANGE_PASSWORD));
	}

	@Test
	@WithMockUser(username = USERNAME)
	@Ignore
	public void testChangePasswordConfirm() throws Exception {
		registerUser(USERNAME, USER_EMAIL);

		mockMvc.perform(put(PROFILE + SETTINGS + PASSWORD).param(PASSWORD, PASSWORD).param(NEW_PASSWORD, NEW_PASSWORD)
				.param(CONFIRM_NEW_PASSWORD, CONFIRM_NEW_PASSWORD)).andExpect(redirectedUrl("hello"));
	}

	private UserServiceModel registerUser(String username, String email) {
		final UserRegisterBindingModel userRegisterBindingModel = new UserRegisterBindingModel();
		userRegisterBindingModel.setUsername(username);
		userRegisterBindingModel.setConfirmPassword(PASSWORD);
		userRegisterBindingModel.setPassword(PASSWORD);

		final ContactRegisterBindingModel contact = new ContactRegisterBindingModel();
		contact.setEmail(email);
		userRegisterBindingModel.setContact(contact);

		return userService.register(modelMapper.map(userRegisterBindingModel, UserServiceModel.class));
	}

}
