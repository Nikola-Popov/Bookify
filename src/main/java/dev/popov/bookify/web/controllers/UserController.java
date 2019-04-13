package dev.popov.bookify.web.controllers;

import static dev.popov.bookify.web.controllers.constants.AuthorizationConstants.IS_ANONYMOUS;
import static dev.popov.bookify.web.controllers.constants.view.UserViewConstants.ALL_USERS;
import static dev.popov.bookify.web.controllers.constants.view.UserViewConstants.LOGIN;
import static dev.popov.bookify.web.controllers.constants.view.UserViewConstants.REGISTER;
import static java.util.stream.Collectors.toList;

import java.security.Principal;
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

import dev.popov.bookify.domain.model.binding.ContactEditBindingModel;
import dev.popov.bookify.domain.model.binding.UserEditBindingModel;
import dev.popov.bookify.domain.model.binding.UserRegisterBindingModel;
import dev.popov.bookify.domain.model.service.UserEditServiceModel;
import dev.popov.bookify.domain.model.service.UserServiceModel;
import dev.popov.bookify.domain.model.view.UserListViewModel;
import dev.popov.bookify.domain.model.view.UserSettingsViewModel;
import dev.popov.bookify.service.user.UserService;

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
	public ModelAndView fetchAll(ModelAndView modelAndView) {
		final List<UserListViewModel> userListViewModels = userService.findAll().stream()
				.map(user -> modelMapper.map(user, UserListViewModel.class)).collect(toList());
		modelAndView.addObject("userListViewModels", userListViewModels);

		return view(ALL_USERS, modelAndView);
	}

	@GetMapping(REGISTER_PATH)
	@PreAuthorize(IS_ANONYMOUS)
	public ModelAndView register(ModelAndView modelAndView,
			@ModelAttribute(name = USER_REGISTER_BINDING_MODEL) UserRegisterBindingModel userRegisterBindingModel) {
		modelAndView.addObject(USER_REGISTER_BINDING_MODEL, userRegisterBindingModel);

		return view(REGISTER, modelAndView);
	}

	@PostMapping(REGISTER_PATH)
	@PreAuthorize(IS_ANONYMOUS)
	public ModelAndView registerConfirm(
			@ModelAttribute(name = USER_REGISTER_BINDING_MODEL) UserRegisterBindingModel userRegisterBindingModel) {
		if (!isRegistrationPasswordsMatch(userRegisterBindingModel)) {
			return view(REGISTER);
		}

		userService.register(modelMapper.map(userRegisterBindingModel, UserServiceModel.class));

		return redirect("/users/login");
	}

	@GetMapping("/login")
	@PreAuthorize(IS_ANONYMOUS)
	public ModelAndView login() {
		return view(LOGIN);
	}

	@PutMapping("/edit/{id}")
	public ModelAndView edit(@PathVariable(name = "id") String id,
			@ModelAttribute(name = "contactEditBindingModel") ContactEditBindingModel contactEditBindingModel) {
		// userService.edit(id, modelMapper.map(contactEditBindingModel,
		// ContactServiceModel.class));
		//
		return redirect(USERS_PATH);
	}

	@DeleteMapping("/delete/{id}")
	public ModelAndView delete(@PathVariable(name = "id") String id) {
		userService.delete(id);

		return redirect(USERS_PATH);
	}

	@GetMapping("/profile/settings")
	public ModelAndView settings(Principal principal, ModelAndView modelAndView) {
		modelAndView.addObject("userSettingsViewModel",
				modelMapper.map(userService.loadUserByUsername(principal.getName()), UserSettingsViewModel.class));

		return view("user_settings", modelAndView);
	}

	@PostMapping("/profile/settings/{id}")
	public ModelAndView settingsConfirm(@PathVariable(name = "id") String id,
			@ModelAttribute(name = "userEditBindingModel") UserEditBindingModel userEditBindingModel) {
		userService.edit(id, modelMapper.map(userEditBindingModel, UserEditServiceModel.class));

		return redirect("/users/profile/settings");
	}

	private boolean isRegistrationPasswordsMatch(UserRegisterBindingModel userRegisterBindingModel) {
		return userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword());
	}
}