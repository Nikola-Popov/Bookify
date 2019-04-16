package dev.popov.bookify.service.role;

import org.springframework.stereotype.Component;

import dev.popov.bookify.domain.entity.Role;

@Component
public class RoleFactory {
	public Role createRole(String authority) {
		return new Role(authority);
	}
}
