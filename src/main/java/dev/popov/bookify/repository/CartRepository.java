package dev.popov.bookify.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.popov.bookify.domain.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {
	Optional<Cart> findByUser_Username(String username);
}
