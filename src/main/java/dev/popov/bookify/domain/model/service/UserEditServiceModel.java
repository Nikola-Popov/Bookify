package dev.popov.bookify.domain.model.service;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserEditServiceModel extends BaseServiceModel {
	private String username;
	private ContactServiceModel contact;
	private String password;
	private MultipartFile image;
}
