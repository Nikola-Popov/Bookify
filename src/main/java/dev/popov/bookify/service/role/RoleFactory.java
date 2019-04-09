package dev.popov.bookify.service.role;

import dev.popov.bookify.domain.entity.Role;

public class RoleFactory {
	public Role createRole(String authority) {
		return new Role(authority);
	}
}
