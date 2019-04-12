package dev.popov.bookify.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.popov.bookify.domain.entity.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase, String> {
}
