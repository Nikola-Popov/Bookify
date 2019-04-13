package dev.popov.bookify.domain.model.binding;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserEditBindingModel {
	private String username;
	private ContactEditBindingModel contact;
}
