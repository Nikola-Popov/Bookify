package dev.popov.bookify.web.controllers;

import static dev.popov.bookify.web.controllers.constants.AuthorizationConstants.HAS_ADMIN_ROLE;
import static dev.popov.bookify.web.controllers.constants.AuthorizationConstants.IS_ANONYMOUS;
import static dev.popov.bookify.web.controllers.constants.AuthorizationConstants.IS_AUTHENTICATED;
import static dev.popov.bookify.web.controllers.constants.view.UserViewConstants.ALL_USERS;
import static dev.popov.bookify.web.controllers.constants.view.UserViewConstants.ERRORS_FORBIDDEN_ACTION_ON_ROOT_ERROR_PAGE;
import static dev.popov.bookify.web.controllers.constants.view.UserViewConstants.LOGIN;
import static dev.popov.bookify.web.controllers.constants.view.UserViewConstants.PROFILE_SETTINGS_PASSWORD;
import static dev.popov.bookify.web.controllers.constants.view.UserViewConstants.REGISTER;
import static dev.popov.bookify.web.controllers.constants.view.UserViewConstants.USER_SETTINGS;
import static dev.popov.bookify.web.controllers.constants.view.UserViewConstants.USER_SETTINGS_CHANGE_PASSWORD;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
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
import dev.popov.bookify.web.annotations.PageTitle;

@Controller
@RequestMapping("/users")
public class UserController extends BaseController {
	private static final String REGISTER_PATH = "/register";
	private static final String USERS_PATH = "/users";
	private static final String USER_REGISTER_BINDING_MODEL = "userRegisterBindingModel";

	private final ModelMapper modelMapper;
	private final UserService userService;

	@Autowired
	public UserController(ModelMapper modelMapper, UserService userService) {
		this.modelMapper = modelMapper;
		this.userService = userService;
	}

	@GetMapping
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
			@ModelAttribute(name = USER_REGISTER_BINDING_MODEL) UserRegisterBindingModel userRegisterBindingModel) {
		if (!passwordsMatch(userRegisterBindingModel.getPassword(), userRegisterBindingModel.getConfirmPassword())) {
			return view(REGISTER);
		}

		userService.register(modelMapper.map(userRegisterBindingModel, UserServiceModel.class));

		return redirect("/users/login");
	}

	@GetMapping("/login")
	@PreAuthorize(IS_ANONYMOUS)
	@PageTitle("Login")
	public ModelAndView login() {
		return view(LOGIN);
	}

	@PutMapping("/edit/{id}")
	@PreAuthorize(HAS_ADMIN_ROLE)
	public ModelAndView edit(@PathVariable(name = "id") String id,
			@ModelAttribute(name = "userEditBindingModel") UserEditBindingModel userEditBindingModel)
			throws IOException {
		userService.edit(id, modelMapper.map(userEditBindingModel, UserEditServiceModel.class));

		return redirect(USERS_PATH);
	}

	@DeleteMapping("/delete/{id}")
	@PreAuthorize(HAS_ADMIN_ROLE)
	public ModelAndView delete(@PathVariable(name = "id") String id) {
		userService.delete(id);

		return redirect(USERS_PATH);
	}

	@GetMapping("/profile/settings")
	@PreAuthorize(IS_AUTHENTICATED)
	@PageTitle("User settings")
	public ModelAndView settings(Principal principal, ModelAndView modelAndView) {
		modelAndView.addObject("userSettingsViewModel",
				modelMapper.map(userService.loadUserByUsername(principal.getName()), UserSettingsViewModel.class));

		return view(USER_SETTINGS, modelAndView);
	}

	@PutMapping("/profile/settings/{id}")
	@PreAuthorize(IS_AUTHENTICATED)
	public ModelAndView settingsConfirm(@PathVariable(name = "id") String id,
			@ModelAttribute(name = "userEditBindingModel") UserSettingsEditBindingModel userSettingsEditBindingModel)
			throws IOException {
		userService.edit(id, modelMapper.map(userSettingsEditBindingModel, UserEditServiceModel.class));

		return redirect("/users/profile/settings");
	}

	@GetMapping("/profile/settings/password")
	@PreAuthorize(IS_AUTHENTICATED)
	@PageTitle("Password change")
	public ModelAndView changePassword(
			@ModelAttribute(name = "userPasswordChangeBindingModel") UserPasswordChangeBindingModel userPasswordChangeBindingModel,
			ModelAndView modelAndView) {
		modelAndView.addObject("userPasswordChangeBindingModel", userPasswordChangeBindingModel);

		return view(USER_SETTINGS_CHANGE_PASSWORD, modelAndView);
	}

	@PutMapping("/profile/settings/password")
	@PreAuthorize(IS_AUTHENTICATED)
	public ModelAndView changePasswordConfirm(Principal principal,
			UserPasswordChangeBindingModel userPasswordChangeBindingModel)
			throws UsernameNotFoundException, IOException {
		if (!passwordsMatch(userPasswordChangeBindingModel.getNewPassword(),
				userPasswordChangeBindingModel.getConfirmNewPassword())) {
			return view(PROFILE_SETTINGS_PASSWORD);
		}

		if (passwordsMatch(userPasswordChangeBindingModel.getPassword(),
				userPasswordChangeBindingModel.getNewPassword())) {
			return view(PROFILE_SETTINGS_PASSWORD);
		}

		userPasswordChangeBindingModel.setPassword(userPasswordChangeBindingModel.getNewPassword());
		userService.edit(
				modelMapper.map(userService.loadUserByUsername(principal.getName()), UserServiceModel.class).getId(),
				modelMapper.map(userPasswordChangeBindingModel, UserEditServiceModel.class));

		return redirect("/users/profile/settings/password");
	}

	@ExceptionHandler(ForbiddenActionOnRootException.class)
	public ModelAndView handleForbiddenActionOnRootException(
			ForbiddenActionOnRootException forbiddenActionOnRootException) {
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("errorMessage",
				ExceptionUtils.getRootCause(forbiddenActionOnRootException).getMessage());
		return view(ERRORS_FORBIDDEN_ACTION_ON_ROOT_ERROR_PAGE, modelAndView);
	}

	private boolean passwordsMatch(String password, String confirmPassword) {
		return password != null && password.equals(confirmPassword);
	}
}