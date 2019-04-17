package dev.popov.bookify.web.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import dev.popov.bookify.web.annotations.PageTitle;

@Component
public class PageTitleInterceptor extends HandlerInterceptorAdapter {
	private static final String BOOKIFY_TITLE_PREFIX = "Bookify - ";
	private static final String TITLE_ATTRIBUTE = "title";

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if (modelAndView == null) {
			modelAndView = new ModelAndView();
		} else {
			if (handler instanceof HandlerMethod) {
				final PageTitle pageTitle = ((HandlerMethod) handler).getMethodAnnotation(PageTitle.class);

				if (pageTitle != null) {
					modelAndView.addObject(TITLE_ATTRIBUTE, BOOKIFY_TITLE_PREFIX + pageTitle.value());
				}
			}
		}
	}
}
