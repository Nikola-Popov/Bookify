package dev.popov.bookify.service.user;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import dev.popov.bookify.commons.exceptions.MissingUserException;
import dev.popov.bookify.domain.entity.Contact;
import dev.popov.bookify.domain.entity.User;
import dev.popov.bookify.domain.model.service.ContactServiceModel;
import dev.popov.bookify.domain.model.service.UserServiceModel;
import dev.popov.bookify.repository.UserRepository;
import dev.popov.bookify.service.role.RoleService;

@Service
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final RoleService roleService;
	private final ModelMapper modelMapper;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	public UserServiceImpl(UserRepository userRepository, RoleService roleService, ModelMapper modelMapper,
			BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userRepository = userRepository;
		this.roleService = roleService;
		this.modelMapper = modelMapper;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Unable to find user by name"));
	}

	@Override
	public UserServiceModel register(UserServiceModel userServiceModel) {
		roleService.seedDefaultRolesInDb();

		if (userRepository.count() == 0) {
			userServiceModel.setAuthorities(roleService.findAllRoles());
		} else {
			userServiceModel.getAuthorities().add(roleService.findByAuthority("ROLE_USER"));
		}

		final User user = modelMapper.map(userServiceModel, User.class);
		user.setPassword(bCryptPasswordEncoder.encode(userServiceModel.getPassword()));

		return modelMapper.map(userRepository.saveAndFlush(user), UserServiceModel.class);
	}

	@Override
	public List<UserServiceModel> findAll() {
		return userRepository.findAll().stream().map(user -> modelMapper.map(user, UserServiceModel.class))
				.collect(toList());
	}

	@Override
	public void edit(String id, ContactServiceModel contactServiceModel) {
		final User user = userRepository.findById(id)
				.orElseThrow(() -> new MissingUserException("Unable to find user by id"));
		forbidActionOnRoot(user);
		user.setContact(modelMapper.map(contactServiceModel, Contact.class));

		userRepository.saveAndFlush(user);
	}

	@Override
	public void delete(String id) {
		forbidActionOnRoot(
				userRepository.findById(id).orElseThrow(() -> new MissingUserException("Unable to find user by id")));
		userRepository.deleteById(id);
	}

	private void forbidActionOnRoot(final User user) {
		if (user.getAuthorities().stream().anyMatch(role -> role.getAuthority().equalsIgnoreCase("ROLE_ROOT"))) {
			throw new AccessDeniedException("You are not eligible to perform actions on ROOT!");
		}
	}
}