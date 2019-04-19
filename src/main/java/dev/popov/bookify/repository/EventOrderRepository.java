package dev.popov.bookify.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.popov.bookify.domain.entity.EventOrder;

@Repository
public interface EventOrderRepository extends JpaRepository<EventOrder, String> {
}
