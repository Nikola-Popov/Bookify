package dev.popov.bookify.domain.model.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserSettingsViewModel extends BaseViewModel {
	private String username;
	private ContactViewModel contact;
}
