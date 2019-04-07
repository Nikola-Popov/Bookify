package dev.popov.bookify.domain.model.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ContactViewModel extends BaseViewModel {
	private String firstName;
	private String lastName;
	private String telephone;
	private String email;
	private String address;
}
