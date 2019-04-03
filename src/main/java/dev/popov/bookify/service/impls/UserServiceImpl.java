package dev.popov.bookify.service.impls;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import dev.popov.bookify.domain.entity.User;
import dev.popov.bookify.domain.model.service.UserServiceModel;
import dev.popov.bookify.repository.UserRepository;
import dev.popov.bookify.service.interfaces.RoleService;
import dev.popov.bookify.service.interfaces.UserService;

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
}
