package dev.popov.bookify.web.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/users")
public class UserController extends BaseController {
	@GetMapping("/register")
	@PreAuthorize("isAnonymous()")
	public ModelAndView register() {
		return view("register");
	}

	@GetMapping("/login")
	@PreAuthorize("isAnonymous()")
	public ModelAndView login() {
		return view("login");
	}
}
