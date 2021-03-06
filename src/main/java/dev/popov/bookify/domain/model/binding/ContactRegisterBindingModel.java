package dev.popov.bookify.domain.model.binding;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ContactRegisterBindingModel {
	@NotNull
	@Email
	@NotEmpty
	private String email;
}
