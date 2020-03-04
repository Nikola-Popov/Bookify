package dev.popov.bookify.service.user;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;

import dev.popov.bookify.domain.model.service.UserEditServiceModel;
import dev.popov.bookify.domain.model.service.UserServiceModel;

public interface UserService extends UserDetailsService {
	UserServiceModel register(UserServiceModel userServiceModel);

	List<UserServiceModel> findAll();

	void edit(String id, UserEditServiceModel userEditServiceModel) throws IOException;

	void delete(String id);

    Optional<UserServiceModel> findByUsername(String username);
}
