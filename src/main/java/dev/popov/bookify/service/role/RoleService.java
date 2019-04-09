package dev.popov.bookify.service.role;

import java.util.Set;

import dev.popov.bookify.domain.model.service.RoleServiceModel;

public interface RoleService {
	void seedDefaultRolesInDb();

	Set<RoleServiceModel> findAllRoles();

	RoleServiceModel findByAuthority(String authority);
}
