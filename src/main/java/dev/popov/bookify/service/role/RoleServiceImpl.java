package dev.popov.bookify.service.role;

import static dev.popov.bookify.commons.constants.RoleConstants.ROLE_ADMIN;
import static dev.popov.bookify.commons.constants.RoleConstants.ROLE_ROOT;
import static dev.popov.bookify.commons.constants.RoleConstants.ROLE_USER;
import static java.util.stream.Collectors.toSet;

import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.popov.bookify.domain.model.service.RoleServiceModel;
import dev.popov.bookify.repository.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService {
	private final RoleRepository roleRepository;
	private final ModelMapper modelMapper;
	private final RoleFactory roleFactory;

	@Autowired
	public RoleServiceImpl(RoleRepository roleRepository, ModelMapper modelMapper, RoleFactory roleFactory) {
		this.roleRepository = roleRepository;
		this.modelMapper = modelMapper;
		this.roleFactory = roleFactory;
	}

	@Override
	public void seedDefaultRolesInDb() {
		if (this.roleRepository.count() == 0) {
			this.roleRepository.saveAndFlush(roleFactory.createRole(ROLE_USER));
			this.roleRepository.saveAndFlush(roleFactory.createRole(ROLE_ADMIN));
			this.roleRepository.saveAndFlush(roleFactory.createRole(ROLE_ROOT));
		}
	}

	@Override
	public Set<RoleServiceModel> findAllRoles() {
		return roleRepository.findAll().stream().map(role -> modelMapper.map(role, RoleServiceModel.class))
				.collect(toSet());
	}

	@Override
	public RoleServiceModel findByAuthority(String authority) {
		return modelMapper.map(roleRepository.findByAuthority(authority), RoleServiceModel.class);
	}

}
