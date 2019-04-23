package dev.popov.bookify.web.interceptors;

import static dev.popov.bookify.web.controllers.constants.common.AuthorizationConstants.IS_AUTHENTICATED;
import static java.util.Arrays.asList;
import static java.util.Locale.ENGLISH;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import dev.popov.bookify.domain.entity.AuditLog;
import dev.popov.bookify.repository.AuditLogRepository;
import dev.popov.bookify.web.annotations.LogAction;

@Component
public class AuditLoggerInterceptor extends HandlerInterceptorAdapter {
	private static final List<String> MODIFICATION_HTTP_METHODS = asList("put", "post", "delete");

	private final AuditLogRepository auditLogRepository;

	@Autowired
	public AuditLoggerInterceptor(AuditLogRepository auditLogRepository) {
		this.auditLogRepository = auditLogRepository;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) {
		if (MODIFICATION_HTTP_METHODS.contains(request.getMethod().toLowerCase(ENGLISH))
				&& isUserAuthenticated(handler)) {
			auditLog(request, handler);
		}
	}

	private boolean isUserAuthenticated(Object handler) {
		if (handler instanceof HandlerMethod) {
			final PreAuthorize preAuthorize = ((HandlerMethod) handler).getMethodAnnotation(PreAuthorize.class);
			return preAuthorize.value().equals(IS_AUTHENTICATED);
		}
		return false;
	}

	private void auditLog(HttpServletRequest request, Object handler) {
		final AuditLog auditLog = new AuditLog();
		setAction(handler, auditLog);
		auditLog.setUsername(request.getUserPrincipal().getName());

		auditLogRepository.saveAndFlush(auditLog);
	}

	private void setAction(Object handler, final AuditLog auditLog) {
		if (handler instanceof HandlerMethod) {
			final LogAction logAction = ((HandlerMethod) handler).getMethodAnnotation(LogAction.class);
			auditLog.setAction(logAction.value());
		}
	}
}
