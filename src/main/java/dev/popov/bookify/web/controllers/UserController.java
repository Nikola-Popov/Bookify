package dev.popov.bookify.web.controllers;

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

import dev.popov.bookify.domain.model.binding.ContactEditBindingModel;
import dev.popov.bookify.domain.model.binding.UserRegisterBindingModel;
import dev.popov.bookify.domain.model.service.ContactServiceModel;
import dev.popov.bookify.domain.model.service.UserServiceModel;
import dev.popov.bookify.domain.model.view.UserListViewModel;
import dev.popov.bookify.service.interfaces.UserService;

@Controller
@RequestMapping("/users")
public class UserController extends BaseController {
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

		return view("all_users", modelAndView);
	}

	@GetMapping("/register")
	@PreAuthorize("isAnonymous()")
	public ModelAndView register(ModelAndView modelAndView,
			@ModelAttribute(name = "userRegisterBindingModel") UserRegisterBindingModel userRegisterBindingModel) {
		modelAndView.addObject("userRegisterBindingModel", userRegisterBindingModel);
		return view("register", modelAndView);
	}

	@PostMapping("/register")
	@PreAuthorize("isAnonymous()")
	public ModelAndView registerConfirm(
			@ModelAttribute(name = "userRegisterBindingModel") UserRegisterBindingModel userRegisterBindingModel) {
		userService.register(modelMapper.map(userRegisterBindingModel, UserServiceModel.class));

		return redirect("/users/login");
	}

	@GetMapping("/login")
	@PreAuthorize("isAnonymous()")
	public ModelAndView login() {
		return view("login");
	}

	@PutMapping("/edit/{id}")
	public ModelAndView edit(@PathVariable(name = "id") String id,
			@ModelAttribute(name = "contactEditBindingModel") ContactEditBindingModel contactEditBindingModel) {
		userService.edit(id, modelMapper.map(contactEditBindingModel, ContactServiceModel.class));

		return redirect("/users");
	}

	@DeleteMapping("/delete/{id}")
	public ModelAndView delete(@PathVariable(name = "id") String id) {
		userService.delete(id);

		return redirect("/users");
	}
}