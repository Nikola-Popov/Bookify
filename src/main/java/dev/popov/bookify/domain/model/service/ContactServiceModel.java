package dev.popov.bookify.domain.model.service;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ContactServiceModel extends BaseServiceModel {
	private String firstName;

	private String lastName;

	private String telephone;

	@NotNull
	@NotEmpty
	@Email(message = "Not a valid email")
	private String email;

	private String address;
}
