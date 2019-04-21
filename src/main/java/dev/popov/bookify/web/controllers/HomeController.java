package dev.popov.bookify.web.controllers;

import static dev.popov.bookify.web.controllers.constants.common.AuthorizationConstants.IS_ANONYMOUS;
import static dev.popov.bookify.web.controllers.constants.common.AuthorizationConstants.IS_AUTHENTICATED;
import static dev.popov.bookify.web.controllers.constants.common.CommonPathConstants.HOME_PATH;
import static dev.popov.bookify.web.controllers.constants.common.CommonPathConstants.INDEX_PATH;
import static dev.popov.bookify.web.controllers.constants.common.CommonViewConstants.HOME;
import static dev.popov.bookify.web.controllers.constants.common.CommonViewConstants.INDEX;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import dev.popov.bookify.web.annotations.PageTitle;

@Controller
public class HomeController extends BaseController {

	@GetMapping(INDEX_PATH)
	@PreAuthorize(IS_ANONYMOUS)
	@PageTitle("Index")
	public ModelAndView index() {
		return view(INDEX);
	}

	@GetMapping(HOME_PATH)
	@PreAuthorize(IS_AUTHENTICATED)
	@PageTitle("Home")
	public ModelAndView home() {
		return view(HOME);
	}
}
