package dev.popov.bookify.domain.model.binding;

import dev.popov.bookify.domain.entity.Contact;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRegisterBindingModel {
	private String username;
	private Contact contact;
	private String password;
	private String confirmPassword;
}
