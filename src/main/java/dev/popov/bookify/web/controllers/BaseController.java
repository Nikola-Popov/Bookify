package dev.popov.bookify.web.controllers;

import org.springframework.web.servlet.ModelAndView;

public abstract class BaseController {

	protected ModelAndView view(String viewName, ModelAndView modelAndView) {
		modelAndView.setViewName(viewName);
		return modelAndView;
	}

	protected ModelAndView view(String view) {
		return view(view, new ModelAndView());
	}
}
