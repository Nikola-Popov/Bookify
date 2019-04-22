package dev.popov.bookify.web.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class FaviconInterceptor extends HandlerInterceptorAdapter {
	private static final String FAVICON = "favicon";
	private static final String FAVICON_LINK = "https://dl1.cbsistatic.com/i/r/2017/05/04/12a4e6fd-88fe-4ca3-b575-ff0e185c2848/thumbnail/32x32/8d6f7ffdcfcb192709e1371c00353b84/imgingest-6193723158694306588.png";

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if (modelAndView != null) {
			modelAndView.addObject(FAVICON, FAVICON_LINK);
		}
	}
}