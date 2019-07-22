package dev.popov.bookify.validation;

import static java.lang.String.format;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;

import dev.popov.bookify.domain.entity.User;
import dev.popov.bookify.domain.model.binding.UserPasswordChangeBindingModel;
import dev.popov.bookify.repository.UserRepository;
import dev.popov.bookify.validation.annotation.Validator;
import dev.popov.bookify.validation.util.PasswordUtil;

@Validator
public class UserPasswordValidator implements org.springframework.validation.Validator {
	private static final String USERNAME_NOT_FOUND = "Username %s was not found";

	private static final String NEW_PASSWORD = "newPassword";
	private static final String PASSWORD = "password";

	private static final String INVALID_CURRENT_PASSWORD = "Invalid current password";
	private static final String PASSWORD_AND_NEW_PASSWORD_ARE_EQUAL = "Password and new password are equal";
	private static final String NEW_PASSWORD_AND_CONFIRM_PASSWORD_DONT_MATCH = "New password and confirm password don`t match";

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final PasswordUtil passwordUtil;

	@Autowired
	public UserPasswordValidator(UserRepository userRepository, PasswordEncoder passwordEncoder,
			PasswordUtil passwordUtil) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.passwordUtil = passwordUtil;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return UserPasswordChangeBindingModel.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		final UserPasswordChangeBindingModel userPasswordChangeBindingModel = (UserPasswordChangeBindingModel) target;

		final User user = this.userRepository.findByUsername(userPasswordChangeBindingModel.getUsername())
				.orElseThrow(() -> new UsernameNotFoundException(
						format(USERNAME_NOT_FOUND, userPasswordChangeBindingModel.getUsername())));
		if (!passwordEncoder.matches(userPasswordChangeBindingModel.getPassword(), user.getPassword())) {
			errors.rejectValue(PASSWORD, INVALID_CURRENT_PASSWORD, INVALID_CURRENT_PASSWORD);
		}

		if (!passwordUtil.passwordsMatch(userPasswordChangeBindingModel.getNewPassword(),
				userPasswordChangeBindingModel.getConfirmNewPassword())) {
			errors.rejectValue(NEW_PASSWORD, NEW_PASSWORD_AND_CONFIRM_PASSWORD_DONT_MATCH,
					NEW_PASSWORD_AND_CONFIRM_PASSWORD_DONT_MATCH);
		}

		if (passwordUtil.passwordsMatch(userPasswordChangeBindingModel.getPassword(),
				userPasswordChangeBindingModel.getNewPassword())) {
			errors.rejectValue(PASSWORD, PASSWORD_AND_NEW_PASSWORD_ARE_EQUAL, PASSWORD_AND_NEW_PASSWORD_ARE_EQUAL);
		}
	}
}
