package dev.popov.bookify.web.controllers;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import dev.popov.bookify.commons.exceptions.EventNotFoundException;
import dev.popov.bookify.commons.exceptions.UserNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler extends BaseController {
	private static final String ERROR_PAGES_PATH = "/errors";

	@ExceptionHandler(Throwable.class)
	public ModelAndView handleAllExceptions(Throwable throwable) {
		return view(ERROR_PAGES_PATH + "/generic_error_page");
	}

	@ExceptionHandler({ UsernameNotFoundException.class, UserNotFoundException.class, EventNotFoundException.class })
	public ModelAndView handleNotFoundExceptions(RuntimeException runtimeException) {
		return view(ERROR_PAGES_PATH + "/not_found_error_page");
	}

}
