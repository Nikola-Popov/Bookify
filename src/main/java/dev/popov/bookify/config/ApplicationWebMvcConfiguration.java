package dev.popov.bookify.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import dev.popov.bookify.web.interceptors.AuditLoggerInterceptor;
import dev.popov.bookify.web.interceptors.PageTitleInterceptor;

@Configuration
public class ApplicationWebMvcConfiguration implements WebMvcConfigurer {
	private final PageTitleInterceptor pageTitleInterceptor;
	private final AuditLoggerInterceptor auditLoggerInterceptor;

	@Autowired
	public ApplicationWebMvcConfiguration(PageTitleInterceptor pageTitleInterceptor,
			AuditLoggerInterceptor auditLoggerInterceptor) {
		this.pageTitleInterceptor = pageTitleInterceptor;
		this.auditLoggerInterceptor = auditLoggerInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(pageTitleInterceptor);
		registry.addInterceptor(auditLoggerInterceptor);
	}
}
