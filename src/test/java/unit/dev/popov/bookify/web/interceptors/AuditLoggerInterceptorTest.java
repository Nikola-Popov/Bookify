package unit.dev.popov.bookify.web.interceptors;

import static dev.popov.bookify.web.controllers.constants.common.AuthorizationConstants.IS_ANONYMOUS;
import static dev.popov.bookify.web.controllers.constants.common.AuthorizationConstants.IS_AUTHENTICATED;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import dev.popov.bookify.AuditLogFactory;
import dev.popov.bookify.domain.entity.AuditLog;
import dev.popov.bookify.repository.AuditLogRepository;
import dev.popov.bookify.web.annotations.LogAction;
import dev.popov.bookify.web.interceptors.AuditLoggerInterceptor;

@RunWith(MockitoJUnitRunner.class)
public class AuditLoggerInterceptorTest {
	private static final String ACTION = "action";
	private static final String PRINCIPAL_NAME = "principalName";

	@InjectMocks
	private AuditLoggerInterceptor auditLoggerInterceptor;

	@Mock
	private HttpServletRequest httpServletRequestMock;

	@Mock
	private HttpServletResponse httpServletResponseMock;

	@Mock
	private HandlerMethod handlerMethodMock;

	@Mock
	private ModelAndView modelAndViewMock;

	@Mock
	private AuditLogRepository auditLogRepositoryMock;

	@Mock
	private Principal principalMock;

	@Mock
	private PreAuthorize preAuthorizeMock;

	@Mock
	private LogAction logActionMock;

	@Mock
	private AuditLogFactory auditLogFactoryMock;

	@Mock
	private AuditLog auditLogMock;

	@Before
	public void setUp() {
		when(httpServletRequestMock.getMethod()).thenReturn("post");

		when(httpServletRequestMock.getUserPrincipal()).thenReturn(principalMock);
		when(principalMock.getName()).thenReturn(PRINCIPAL_NAME);

		when(handlerMethodMock.getMethodAnnotation(PreAuthorize.class)).thenReturn(preAuthorizeMock);
		when(preAuthorizeMock.value()).thenReturn(IS_AUTHENTICATED);

		when(handlerMethodMock.getMethodAnnotation(LogAction.class)).thenReturn(logActionMock);
		when(logActionMock.value()).thenReturn(ACTION);

		when(auditLogFactoryMock.createAuditLog(PRINCIPAL_NAME, ACTION)).thenReturn(auditLogMock);
	}

	@Test
	public void testPostHandleDoesntLogWhenReadHttpRequest() {
		when(httpServletRequestMock.getMethod()).thenReturn("get");

		auditLoggerInterceptor.postHandle(httpServletRequestMock, httpServletResponseMock, handlerMethodMock,
				modelAndViewMock);

		verify(auditLogRepositoryMock, never()).saveAndFlush(any(AuditLog.class));
	}

	@Test
	public void testPostHandleDoesntLogWhenUserNotAuthenticated() {
		when(preAuthorizeMock.value()).thenReturn(IS_ANONYMOUS);

		auditLoggerInterceptor.postHandle(httpServletRequestMock, httpServletResponseMock, handlerMethodMock,
				modelAndViewMock);

		verify(auditLogRepositoryMock, never()).saveAndFlush(any(AuditLog.class));
	}

	@Test
	public void testPostHandleAuditLogs() {
		auditLoggerInterceptor.postHandle(httpServletRequestMock, httpServletResponseMock, handlerMethodMock,
				modelAndViewMock);

		verify(auditLogRepositoryMock).saveAndFlush(auditLogMock);
	}
}
