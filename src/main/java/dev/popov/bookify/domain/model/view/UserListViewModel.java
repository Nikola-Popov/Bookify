package dev.popov.bookify.domain.model.view;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserListViewModel extends BaseViewModel {
	private String username;
	private Set<RoleViewModel> authorities;
	private ContactViewModel contact;
}
