package dev.popov.bookify.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import dev.popov.bookify.domain.model.binding.UserRegisterBindingModel;
import dev.popov.bookify.domain.model.service.UserServiceModel;
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
}
