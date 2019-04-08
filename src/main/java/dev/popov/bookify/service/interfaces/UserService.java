package dev.popov.bookify.service.interfaces;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import dev.popov.bookify.domain.model.service.ContactServiceModel;
import dev.popov.bookify.domain.model.service.UserServiceModel;

public interface UserService extends UserDetailsService {
	UserServiceModel register(UserServiceModel userServiceModel);

	List<UserServiceModel> findAll();

	void edit(String id, ContactServiceModel contactServiceModel);

	void delete(String id);
}
