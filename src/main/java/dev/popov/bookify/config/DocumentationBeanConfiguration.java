package dev.popov.bookify.config;

import static java.lang.String.format;
import static springfox.documentation.builders.PathSelectors.any;
import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;
import static springfox.documentation.swagger.web.UiConfigurationBuilder.builder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class DocumentationBeanConfiguration {
	private static final int HIDE_MODELS = -1;
	private static final String PROJECT_BASE_PACKAGE = "dev.popov.bookify";
	private static final String DOCUMENTATION_VERSION = "1.0";
	private static final String DOCUMENTATION_DESCRIPTION_TEMPLATE = "Internal REST API Documentation for BOOKIFY API v%s";
	private static final String DOCUMENTATION_TITLE = "Internal BOOKIFY REST API";

	@Bean
	public Docket createSwagger() {
		return new Docket(SWAGGER_2)
				.select()
				.apis(basePackage(PROJECT_BASE_PACKAGE))
				.paths(any())
				.build()
				.apiInfo(
						new ApiInfoBuilder()
								.version(DOCUMENTATION_VERSION)
								.title(DOCUMENTATION_TITLE)
								.description(format(DOCUMENTATION_DESCRIPTION_TEMPLATE, DOCUMENTATION_VERSION))
								.build());
	}

	@Bean
	public UiConfiguration createUIConfig() {
		return builder()
				.defaultModelsExpandDepth(HIDE_MODELS)
				.defaultModelExpandDepth(HIDE_MODELS)
				.build();
	}
}
