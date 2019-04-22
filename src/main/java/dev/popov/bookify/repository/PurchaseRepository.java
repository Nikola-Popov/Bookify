package dev.popov.bookify.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.popov.bookify.domain.entity.Purchase;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, String> {
	List<Purchase> findAllByUser_Username(String username);
}
