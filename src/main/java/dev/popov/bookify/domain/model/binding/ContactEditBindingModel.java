package dev.popov.bookify.domain.model.binding;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ContactEditBindingModel {
	// TODO validation
	private String firstName;
	private String lastName;
	private String telephone;
	private String email;
	private String address;
}
