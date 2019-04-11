package dev.popov.bookify.web.controllers;

import static dev.popov.bookify.web.controllers.constants.AuthorizationConstants.IS_ANONYMOUS;
import static dev.popov.bookify.web.controllers.constants.PathConstants.HOME_PATH;
import static dev.popov.bookify.web.controllers.constants.PathConstants.INDEX_PATH;
import static dev.popov.bookify.web.controllers.constants.view.HomeViewConstants.HOME;
import static dev.popov.bookify.web.controllers.constants.view.HomeViewConstants.INDEX;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController extends BaseController {

	@GetMapping(INDEX_PATH)
	@PreAuthorize(IS_ANONYMOUS)
	public ModelAndView index() {
		return view(INDEX);
	}

	@GetMapping(HOME_PATH)
	public ModelAndView home() {
		return view(HOME);
	}
}
