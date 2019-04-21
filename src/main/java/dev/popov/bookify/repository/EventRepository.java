package dev.popov.bookify.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.popov.bookify.domain.entity.Event;
import dev.popov.bookify.domain.entity.EventType;

@Repository
public interface EventRepository extends JpaRepository<Event, String> {
	List<Event> findAllByEventType(EventType eventType);

	void deleteAllByExpiresOnIsBefore(LocalDate localDate);
}
