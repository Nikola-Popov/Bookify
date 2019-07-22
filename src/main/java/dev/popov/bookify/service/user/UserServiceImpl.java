package dev.popov.bookify.service.user;

import static dev.popov.bookify.commons.constants.RoleConstants.ROLE_ROOT;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.IOException;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dev.popov.bookify.commons.constants.RoleConstants;
import dev.popov.bookify.commons.exceptions.ForbiddenActionOnRootException;
import dev.popov.bookify.commons.exceptions.UserNotFoundException;
import dev.popov.bookify.domain.entity.Contact;
import dev.popov.bookify.domain.entity.User;
import dev.popov.bookify.domain.model.service.ContactServiceModel;
import dev.popov.bookify.domain.model.service.UserEditServiceModel;
import dev.popov.bookify.domain.model.service.UserServiceModel;
import dev.popov.bookify.repository.UserRepository;
import dev.popov.bookify.service.cloud.CloudinaryService;
import dev.popov.bookify.service.role.RoleService;

@Service
public class UserServiceImpl implements UserService {
	private static final String UNABLE_TO_FIND_USER_BY_NAME_MESSAGE = "Unable to find user by name";
	private static final String UNABLE_TO_FIND_USER_BY_ID_MESSAGE = "Unable to find user by id!";
	private static final String NOT_ELIGIBLE_TO_MODIFY_ROOT_MESSAGE = "You are not eligible to perform actions on ROOT!";

	private final UserRepository userRepository;
	private final RoleService roleService;
	private final ModelMapper modelMapper;
	private final PasswordEncoder passwordEncoder;
	private final CloudinaryService cloudinaryService;

	@Autowired
	public UserServiceImpl(UserRepository userRepository, RoleService roleService, ModelMapper modelMapper,
			PasswordEncoder passwordEncoder, CloudinaryService cloudinaryService) {
		this.userRepository = userRepository;
		this.roleService = roleService;
		this.modelMapper = modelMapper;
		this.passwordEncoder = passwordEncoder;
		this.cloudinaryService = cloudinaryService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException(UNABLE_TO_FIND_USER_BY_NAME_MESSAGE));
	}

	@Override
	public UserServiceModel register(UserServiceModel userServiceModel) {
		roleService.seedDefaultRolesInDb();

		if (userRepository.count() == 0) {
			userServiceModel.setAuthorities(roleService.findAllRoles());
		} else {
			userServiceModel.getAuthorities().add(roleService.findByAuthority(RoleConstants.ROLE_USER));
		}

		final User user = modelMapper.map(userServiceModel, User.class);
		user.setPassword(passwordEncoder.encode(userServiceModel.getPassword()));

		return modelMapper.map(userRepository.saveAndFlush(user), UserServiceModel.class);
	}

	@Override
	public List<UserServiceModel> findAll() {
		return userRepository.findAll().stream().map(user -> modelMapper.map(user, UserServiceModel.class))
				.collect(toList());
	}

	@Override
	public void edit(String id, UserEditServiceModel userEditServiceModel) throws IOException {
		final User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException(UNABLE_TO_FIND_USER_BY_ID_MESSAGE));
		forbidActionOnRoot(user);

		final ContactServiceModel contact = userEditServiceModel.getContact();
		if (contact != null) {
			contact.setId(user.getContact().getId());
			user.setContact(modelMapper.map(contact, Contact.class));
		}

		if (isNotBlank(userEditServiceModel.getUsername())) {
			user.setUsername(userEditServiceModel.getUsername());
		}

		if (isNotBlank(userEditServiceModel.getPassword())) {
			user.setPassword(passwordEncoder.encode(userEditServiceModel.getPassword()));
		}

		if (isImageSelected(userEditServiceModel.getImage())) {
			user.setImage(cloudinaryService.uploadImage(userEditServiceModel.getImage()));
		}

		userRepository.saveAndFlush(user);
	}

	@Override
	public void delete(String id) {
		forbidActionOnRoot(userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException(UNABLE_TO_FIND_USER_BY_ID_MESSAGE)));
		userRepository.deleteById(id);
	}

	private boolean isImageSelected(MultipartFile image) {
		return image != null && !image.isEmpty();
	}

	private void forbidActionOnRoot(final User user) {
		if (user.getAuthorities().stream().anyMatch(role -> role.getAuthority().equalsIgnoreCase(ROLE_ROOT))) {
			throw new ForbiddenActionOnRootException(NOT_ELIGIBLE_TO_MODIFY_ROOT_MESSAGE);
		}
	}
}
