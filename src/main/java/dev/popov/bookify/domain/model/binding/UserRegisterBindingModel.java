package dev.popov.bookify.domain.model.binding;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRegisterBindingModel {
	private String username;
	private ContactEditBindingModel contact;
	private String password;
	private String confirmPassword;
}
