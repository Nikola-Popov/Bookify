package unit.dev.popov.bookify.web.interceptors;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import dev.popov.bookify.web.annotations.PageTitle;
import dev.popov.bookify.web.interceptors.PageTitleInterceptor;

@RunWith(MockitoJUnitRunner.class)
public class PageTitleInterceptorTest {
	private static final String VALUE = "value";
	private static final String BOOKIFY_TITLE_PREFIX = "Bookify - ";
	private static final String TITLE_ATTRIBUTE = "title";

	@InjectMocks
	private PageTitleInterceptor pageTitleInterceptor;

	@Mock
	private HttpServletRequest httpServletRequestMock;

	@Mock
	private HttpServletResponse httpServletResponseMock;

	@Mock
	private HandlerMethod handlerMethodMock;

	@Mock
	private ModelAndView modelAndViewMock;

	@Mock
	private PageTitle pageTitleMock;

	@Before
	public void setUp() {
		when(handlerMethodMock.getMethodAnnotation(PageTitle.class)).thenReturn(pageTitleMock);
	}

	@Test
	public void testPostHandleNoObjectIsAttachedWhenPageTitleAnnotationIsMissing() throws Exception {
		when(handlerMethodMock.getMethodAnnotation(PageTitle.class)).thenReturn(null);

		pageTitleInterceptor.postHandle(httpServletRequestMock, httpServletResponseMock, handlerMethodMock,
				modelAndViewMock);

		verify(modelAndViewMock, never()).addObject(anyString(), anyString());
	}

	@Test
	public void testPostHandleSetsPageTitle() throws Exception {
		when(pageTitleMock.value()).thenReturn(VALUE);

		pageTitleInterceptor.postHandle(httpServletRequestMock, httpServletResponseMock, handlerMethodMock,
				modelAndViewMock);

		verify(modelAndViewMock).addObject(TITLE_ATTRIBUTE, BOOKIFY_TITLE_PREFIX + VALUE);
	}

}
