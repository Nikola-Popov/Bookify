package dev.popov.bookify.domain.model.binding;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserPasswordChangeBindingModel {
	private String password;
	private String newPassword;
	private String confirmNewPassword;
}
