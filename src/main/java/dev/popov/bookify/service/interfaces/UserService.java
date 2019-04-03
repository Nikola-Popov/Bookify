package dev.popov.bookify.service.interfaces;

import org.springframework.security.core.userdetails.UserDetailsService;

import dev.popov.bookify.domain.model.service.UserServiceModel;

public interface UserService extends UserDetailsService {
	UserServiceModel register(UserServiceModel userServiceModel);
}
