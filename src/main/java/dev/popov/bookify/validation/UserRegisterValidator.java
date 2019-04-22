package dev.popov.bookify.validation;

import static org.apache.commons.lang3.StringUtils.isBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;

import dev.popov.bookify.domain.entity.User;
import dev.popov.bookify.domain.model.binding.UserRegisterBindingModel;
import dev.popov.bookify.repository.UserRepository;
import dev.popov.bookify.validation.annotation.Validator;
import dev.popov.bookify.validation.util.PasswordUtil;

@Validator
public class UserRegisterValidator implements org.springframework.validation.Validator {
	private static final String EMAIL = "email";
	private static final String EMAIL_MUST_NOT_BE_BLANK = "Email must not be blank";
	private static final String USERNAME = "username";
	private static final String USERNAME_IS_ALREADY_TAKEN = "Username is already taken";
	private static final String PASSWORDS_DO_NOT_MATCH = "Passwords do not match";
	private static final String CONFIRM_PASSWORD = "confirmPassword";

	private final UserRepository userRepository;
	private final PasswordUtil passwordUtil;

	@Autowired
	public UserRegisterValidator(UserRepository userRepository, PasswordUtil passwordUtil) {
		this.userRepository = userRepository;
		this.passwordUtil = passwordUtil;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return UserRegisterBindingModel.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		final UserRegisterBindingModel userRegisterBindingModel = (UserRegisterBindingModel) target;

		final User user = this.userRepository.findByUsername(userRegisterBindingModel.getUsername()).orElse(null);
		if (user != null) {
			errors.rejectValue(USERNAME, USERNAME_IS_ALREADY_TAKEN, USERNAME_IS_ALREADY_TAKEN);
		}

		if (!passwordUtil.passwordsMatch(userRegisterBindingModel.getPassword(),
				userRegisterBindingModel.getConfirmPassword())) {
			errors.rejectValue(CONFIRM_PASSWORD, PASSWORDS_DO_NOT_MATCH, PASSWORDS_DO_NOT_MATCH);
		}

		if (userRegisterBindingModel.getContact() == null
				|| isBlank(userRegisterBindingModel.getContact().getEmail())) {
			errors.rejectValue(EMAIL, EMAIL_MUST_NOT_BE_BLANK, EMAIL_MUST_NOT_BE_BLANK);
		}
	}
}
