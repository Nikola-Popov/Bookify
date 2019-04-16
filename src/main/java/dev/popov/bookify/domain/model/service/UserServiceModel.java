package dev.popov.bookify.domain.model.service;

import static dev.popov.bookify.commons.constants.UserSetupConstants.PASSWORD_LENGTH;
import static dev.popov.bookify.commons.constants.UserSetupConstants.PASSWORD_TOO_SHORT_MESSAGE;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserServiceModel extends BaseServiceModel {
	@NotEmpty
	@NotNull
	private String username;

	@NotNull
	@Size(min = PASSWORD_LENGTH, message = PASSWORD_TOO_SHORT_MESSAGE)
	private String password;

	private Set<RoleServiceModel> authorities = new HashSet<>();

	private ContactServiceModel contact;
}
