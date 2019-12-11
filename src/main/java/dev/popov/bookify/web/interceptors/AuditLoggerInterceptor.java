package dev.popov.bookify.web.interceptors;

import static dev.popov.bookify.web.controllers.constants.common.AuthorizationConstants.IS_AUTHENTICATED;
import static java.util.Arrays.asList;
import static java.util.Locale.ENGLISH;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.UrlPathHelper;

import dev.popov.bookify.domain.entity.AuditLog;
import dev.popov.bookify.repository.AuditLogRepository;
import dev.popov.bookify.web.annotations.LogAction;
import dev.popov.bookify.web.factories.AuditLogFactory;

@Component
public class AuditLoggerInterceptor extends HandlerInterceptorAdapter {
	private static final List<String> MODIFICATION_HTTP_METHODS = asList("put", "post", "delete");

	private final AuditLogRepository auditLogRepository;
	private final AuditLogFactory auditLogFactory;
	private final UrlPathHelper urlPathHelper;

	@Autowired
	public AuditLoggerInterceptor(AuditLogRepository auditLogRepository, AuditLogFactory auditLogFactory,
			UrlPathHelper urlPathHelper) {
		this.auditLogRepository = auditLogRepository;
		this.auditLogFactory = auditLogFactory;
		this.urlPathHelper = urlPathHelper;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) {
		if (isSocialLogin(request)) {
			return;
		}

		if (MODIFICATION_HTTP_METHODS.contains(request.getMethod().toLowerCase(ENGLISH))
				&& isUserAuthenticated(handler)) {
			final AuditLog auditLog = auditLogFactory.createAuditLog(request.getUserPrincipal().getName(),
					extractAction(handler));

			auditLogRepository.saveAndFlush(auditLog);
		}
	}

	private boolean isSocialLogin(HttpServletRequest request) {
		return urlPathHelper.getPathWithinApplication(request).endsWith("/connect/facebook");
	}

	private boolean isUserAuthenticated(Object handler) {
		if (handler instanceof HandlerMethod) {
			final PreAuthorize preAuthorize = ((HandlerMethod) handler).getMethodAnnotation(PreAuthorize.class);
			return preAuthorize.value().equals(IS_AUTHENTICATED);
		}
		return false;
	}

	private String extractAction(Object handler) {
		if (handler instanceof HandlerMethod) {
			final LogAction logAction = ((HandlerMethod) handler).getMethodAnnotation(LogAction.class);
			return logAction.value();
		}
		return EMPTY;
	}
}
