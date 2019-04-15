package dev.popov.bookify.domain.model.binding;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserSettingsEditBindingModel extends UserEditBindingModel {
	private MultipartFile image;
}
