package dev.popov.bookify.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.popov.bookify.domain.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
	Optional<User> findByUsername(String username);
}
