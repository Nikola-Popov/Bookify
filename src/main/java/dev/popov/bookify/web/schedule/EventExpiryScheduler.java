package dev.popov.bookify.web.schedule;

import static java.time.LocalDate.now;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import dev.popov.bookify.repository.EventRepository;

@Component
public class EventExpiryScheduler {
	private static final String EVERY_DAY = "* * */24 * * *";

	@Autowired
	private EventRepository eventRepository;

	@Transactional
	@Scheduled(cron = EVERY_DAY)
	public void removeExpiredEvents() {
		eventRepository.deleteAllByExpiresOnIsBefore(now());
	}
}
