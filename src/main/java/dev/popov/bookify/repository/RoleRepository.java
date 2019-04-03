package dev.popov.bookify.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.popov.bookify.domain.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
	Role findByAuthority(String authority);
}
