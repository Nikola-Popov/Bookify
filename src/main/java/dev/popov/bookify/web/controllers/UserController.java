package dev.popov.bookify.web.controllers;

import static dev.popov.bookify.web.controllers.constants.common.AuthorizationConstants.HAS_ADMIN_ROLE;
import static dev.popov.bookify.web.controllers.constants.common.AuthorizationConstants.IS_ANONYMOUS;
import static dev.popov.bookify.web.controllers.constants.common.AuthorizationConstants.IS_AUTHENTICATED;
import static dev.popov.bookify.web.controllers.constants.user.UserPathConstants.DELETE;
import static dev.popov.bookify.web.controllers.constants.user.UserPathConstants.EDIT;
import static dev.popov.bookify.web.controllers.constants.user.UserPathConstants.LOGIN_PATH;
import static dev.popov.bookify.web.controllers.constants.user.UserPathConstants.PASSWORD;
import static dev.popov.bookify.web.controllers.constants.user.UserPathConstants.PROFILE;
import static dev.popov.bookify.web.controllers.constants.user.UserPathConstants.REGISTER_PATH;
import static dev.popov.bookify.web.controllers.constants.user.UserPathConstants.SETTINGS;
import static dev.popov.bookify.web.controllers.constants.user.UserPathConstants.USERS;
import static dev.popov.bookify.web.controllers.constants.user.UserViewConstants.ALL_USERS;
import static dev.popov.bookify.web.controllers.constants.user.UserViewConstants.ERRORS_FORBIDDEN_ACTION_ON_ROOT_ERROR_PAGE;
import static dev.popov.bookify.web.controllers.constants.user.UserViewConstants.LOGIN;
import static dev.popov.bookify.web.controllers.constants.user.UserViewConstants.REGISTER;
import static dev.popov.bookify.web.controllers.constants.user.UserViewConstants.USER_SETTINGS;
import static dev.popov.bookify.web.controllers.constants.user.UserViewConstants.USER_SETTINGS_CHANGE_PASSWORD;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import dev.popov.bookify.commons.exceptions.ForbiddenActionOnRootException;
import dev.popov.bookify.domain.model.binding.UserEditBindingModel;
import dev.popov.bookify.domain.model.binding.UserPasswordChangeBindingModel;
import dev.popov.bookify.domain.model.binding.UserRegisterBindingModel;
import dev.popov.bookify.domain.model.binding.UserSettingsEditBindingModel;
import dev.popov.bookify.domain.model.service.UserEditServiceModel;
import dev.popov.bookify.domain.model.service.UserServiceModel;
import dev.popov.bookify.domain.model.view.UserListViewModel;
import dev.popov.bookify.domain.model.view.UserSettingsViewModel;
import dev.popov.bookify.service.user.UserService;
import dev.popov.bookify.validation.UserPasswordValidator;
import dev.popov.bookify.validation.UserRegisterValidator;
import dev.popov.bookify.web.annotations.PageTitle;

@Controller
@RequestMapping("/users")
public class UserController extends BaseController {
	private static final String USER_PASSWORD_CHANGE_BINDING_MODEL = "userPasswordChangeBindingModel";
	private static final String USER_REGISTER_BINDING_MODEL = "userRegisterBindingModel";

	private final ModelMapper modelMapper;
	private final UserService userService;
	private final UserPasswordValidator userPasswordValidator;
	private final UserRegisterValidator userRegisterValidator;

	@Autowired
	public UserController(ModelMapper modelMapper, UserService userService, UserPasswordValidator userPasswordValidator,
			UserRegisterValidator userRegisterValidator) {
		this.modelMapper = modelMapper;
		this.userService = userService;
		this.userPasswordValidator = userPasswordValidator;
		this.userRegisterValidator = userRegisterValidator;
	}

	@GetMapping
	@PreAuthorize(HAS_ADMIN_ROLE)
	@PageTitle("All users")
	public ModelAndView fetchAll(ModelAndView modelAndView) {
		final List<UserListViewModel> userListViewModels = userService.findAll().stream()
				.map(user -> modelMapper.map(user, UserListViewModel.class)).collect(toList());
		modelAndView.addObject("userListViewModels", userListViewModels);

		return view(ALL_USERS, modelAndView);
	}

	@GetMapping(REGISTER_PATH)
	@PreAuthorize(IS_ANONYMOUS)
	@PageTitle("Register")
	public ModelAndView register(ModelAndView modelAndView,
			@ModelAttribute(name = USER_REGISTER_BINDING_MODEL) UserRegisterBindingModel userRegisterBindingModel) {
		modelAndView.addObject(USER_REGISTER_BINDING_MODEL, userRegisterBindingModel);

		return view(REGISTER, modelAndView);
	}

	@PostMapping(REGISTER_PATH)
	@PreAuthorize(IS_ANONYMOUS)
	public ModelAndView registerConfirm(
			@Valid @ModelAttribute(name = USER_REGISTER_BINDING_MODEL) UserRegisterBindingModel userRegisterBindingModel,
			BindingResult bindingResult) {
		userRegisterValidator.validate(userRegisterBindingModel, bindingResult);
		if (bindingResult.hasErrors()) {
			return view(REGISTER);
		}

		userService.register(modelMapper.map(userRegisterBindingModel, UserServiceModel.class));

		return redirect(USERS + LOGIN_PATH);
	}

	@GetMapping(LOGIN_PATH)
	@PreAuthorize(IS_ANONYMOUS)
	@PageTitle("Login")
	public ModelAndView login() {
		return view(LOGIN);
	}

	@PutMapping(EDIT + "/{id}")
	@PreAuthorize(HAS_ADMIN_ROLE)
	public ModelAndView edit(@PathVariable(name = "id") String id,
			@ModelAttribute(name = "userEditBindingModel") UserEditBindingModel userEditBindingModel)
			throws IOException {
		userService.edit(id, modelMapper.map(userEditBindingModel, UserEditServiceModel.class));

		return redirect(USERS);
	}

	@DeleteMapping(DELETE + "/{id}")
	@PreAuthorize(HAS_ADMIN_ROLE)
	public ModelAndView delete(@PathVariable(name = "id") String id) {
		userService.delete(id);

		return redirect(USERS);
	}

	@GetMapping(PROFILE + SETTINGS)
	@PreAuthorize(IS_AUTHENTICATED)
	@PageTitle("User settings")
	public ModelAndView settings(Principal principal, ModelAndView modelAndView) {
		modelAndView.addObject("userSettingsViewModel",
				modelMapper.map(userService.loadUserByUsername(principal.getName()), UserSettingsViewModel.class));

		return view(USER_SETTINGS, modelAndView);
	}

	@PutMapping(PROFILE + SETTINGS + "/{id}")
	@PreAuthorize(IS_AUTHENTICATED)
	public ModelAndView settingsConfirm(@PathVariable(name = "id") String id,
			@Valid @ModelAttribute(name = "userEditBindingModel") UserSettingsEditBindingModel userSettingsEditBindingModel,
			BindingResult bindingResult) throws IOException {
		if (bindingResult.hasErrors()) {
			return view(USER_SETTINGS);
		}
		userService.edit(id, modelMapper.map(userSettingsEditBindingModel, UserEditServiceModel.class));

		return redirect(USERS + PROFILE + SETTINGS);
	}

	@GetMapping(PROFILE + SETTINGS + PASSWORD)
	@PreAuthorize(IS_AUTHENTICATED)
	@PageTitle("Password change")
	public ModelAndView changePassword(Principal principal,
			@ModelAttribute(name = USER_PASSWORD_CHANGE_BINDING_MODEL) UserPasswordChangeBindingModel userPasswordChangeBindingModel,
			ModelAndView modelAndView) {
		userPasswordChangeBindingModel.setUsername(principal.getName());
		modelAndView.addObject(USER_PASSWORD_CHANGE_BINDING_MODEL, userPasswordChangeBindingModel);

		return view(USER_SETTINGS_CHANGE_PASSWORD, modelAndView);
	}

	@PutMapping(PROFILE + SETTINGS + PASSWORD)
	@PreAuthorize(IS_AUTHENTICATED)
	public ModelAndView changePasswordConfirm(
			@Valid @ModelAttribute(name = USER_PASSWORD_CHANGE_BINDING_MODEL) UserPasswordChangeBindingModel userPasswordChangeBindingModel,
			BindingResult bindingResult) throws UsernameNotFoundException, IOException {
		userPasswordValidator.validate(userPasswordChangeBindingModel, bindingResult);

		if (bindingResult.hasErrors()) {
			return view(USER_SETTINGS_CHANGE_PASSWORD);
		}

		userPasswordChangeBindingModel.setPassword(userPasswordChangeBindingModel.getNewPassword());
		userService.edit(
				modelMapper.map(userService.loadUserByUsername(userPasswordChangeBindingModel.getUsername()),
						UserServiceModel.class).getId(),
				modelMapper.map(userPasswordChangeBindingModel, UserEditServiceModel.class));

		return redirect(USERS + PROFILE + SETTINGS + PASSWORD);
	}

	@ExceptionHandler(ForbiddenActionOnRootException.class)
	public ModelAndView handleForbiddenActionOnRootException(
			ForbiddenActionOnRootException forbiddenActionOnRootException) {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("errorMessage",
				ExceptionUtils.getRootCause(forbiddenActionOnRootException).getMessage());
		return view(ERRORS_FORBIDDEN_ACTION_ON_ROOT_ERROR_PAGE, modelAndView);
	}
}