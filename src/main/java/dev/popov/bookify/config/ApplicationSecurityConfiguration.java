package dev.popov.bookify.config;

import static com.google.common.collect.Iterables.toArray;
import static dev.popov.bookify.commons.constants.RoleConstants.ADMIN;
import static dev.popov.bookify.commons.constants.RoleConstants.ROOT;
import static java.util.Arrays.asList;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfiguration extends WebSecurityConfigurerAdapter {
	private static final String PASSWORD = "password";
	private static final String USERNAME = "username";
	private static final String USERS_LOGOUT = "/users/logout";
	private static final String HOME = "/home";
	private static final String INDEX = "/";
	private static final String USERS_REGISTER = "/users/register";
	private static final String USERS_LOGIN = "/users/login";
	private static final List<String> UI_DIRS_WHITELIST = asList("/css/**", "/js/**", "/img/**", "/scss/**",
			"/vendor/**");
	private static final List<String> SWAGGER_PATHS = asList("/v2/api-docs", "/swagger-ui.html");

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().disable().csrf().disable().authorizeRequests().antMatchers(toArray(SWAGGER_PATHS, String.class))
				.hasAnyRole(ROOT, ADMIN).antMatchers(toArray(UI_DIRS_WHITELIST, String.class)).permitAll()
				.antMatchers(INDEX, USERS_LOGIN, USERS_REGISTER, "/connect/**").anonymous().anyRequest().authenticated()
				.and().formLogin().loginPage(USERS_LOGIN).usernameParameter(USERNAME).passwordParameter(PASSWORD)
				.defaultSuccessUrl(HOME).and().logout().logoutUrl(USERS_LOGOUT).invalidateHttpSession(true)
				.logoutSuccessUrl(INDEX);
	}

	// TODO incorporate csrf
	private CsrfTokenRepository createCsrfTokenRepository() {
		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setSessionAttributeName("_csrf");
		return repository;
	}
}
