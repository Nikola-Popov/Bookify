package dev.popov.bookify.service.impls;

import static java.util.stream.Collectors.toSet;

import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.popov.bookify.domain.entity.Role;
import dev.popov.bookify.domain.model.service.RoleServiceModel;
import dev.popov.bookify.repository.RoleRepository;
import dev.popov.bookify.service.interfaces.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
	private final RoleRepository roleRepository;
	private final ModelMapper modelMapper;

	@Autowired
	public RoleServiceImpl(RoleRepository roleRepository, ModelMapper modelMapper) {
		this.roleRepository = roleRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public void seedDefaultRolesInDb() {
		if (this.roleRepository.count() == 0) {
			this.roleRepository.saveAndFlush(new Role("ROLE_USER"));
			this.roleRepository.saveAndFlush(new Role("ROLE_ADMIN"));
			this.roleRepository.saveAndFlush(new Role("ROLE_ROOT"));
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
