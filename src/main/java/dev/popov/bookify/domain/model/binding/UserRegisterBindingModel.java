package dev.popov.bookify.domain.model.binding;

import static dev.popov.bookify.commons.constants.UserSetupConstants.PASSWORD_LENGTH;
import static dev.popov.bookify.commons.constants.UserSetupConstants.PASSWORD_TOO_SHORT_MESSAGE;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRegisterBindingModel {
	@NotNull
	@NotEmpty
	private String username;
	@NotNull
	private ContactRegisterBindingModel contact;
	@NotNull
	@Size(min = PASSWORD_LENGTH, message = PASSWORD_TOO_SHORT_MESSAGE)
	private String password;
	@NotNull
	@Size(min = PASSWORD_LENGTH, message = PASSWORD_TOO_SHORT_MESSAGE)
	private String confirmPassword;
}
