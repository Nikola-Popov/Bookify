package dev.popov.bookify.domain.model.service;

import java.util.Set;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import dev.popov.bookify.domain.entity.Contact;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserServiceModel {
	@NotEmpty
	@NotNull
	private String username;

	@NotEmpty
	@NotNull
	@Size(min = 8, message = "Password must be atleast 8 characters long")
	private String password;

	private Set<RoleServiceModel> authorities;

	private Contact contact;
}
