package dev.popov.bookify.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import dev.popov.bookify.commons.FileFactory;
import dev.popov.bookify.service.role.RoleFactory;

@Configuration
public class ApplicationBeanConfiguration {

	@Bean
	public ModelMapper createModelMapper() {
		return new ModelMapper();
	}

	@Bean
	public BCryptPasswordEncoder createBCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public RoleFactory createRoleFactory() {
		return new RoleFactory();
	}

	@Bean
	public FileFactory createFileFactory() {
		return new FileFactory();
	}
}
