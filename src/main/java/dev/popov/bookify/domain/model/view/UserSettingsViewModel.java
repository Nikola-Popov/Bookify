package dev.popov.bookify.domain.model.view;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserSettingsViewModel extends BaseViewModel {
	private String username;
	private ContactViewModel contact;
	private MultipartFile image;
}
