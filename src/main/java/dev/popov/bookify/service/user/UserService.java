package dev.popov.bookify.service.user;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import dev.popov.bookify.domain.model.service.UserEditServiceModel;
import dev.popov.bookify.domain.model.service.UserServiceModel;

public interface UserService extends UserDetailsService {
	UserServiceModel register(UserServiceModel userServiceModel);

	List<UserServiceModel> findAll();

	void edit(String id, UserEditServiceModel userEditServiceModel);

	void delete(String id);
}
