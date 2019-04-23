package dev.popov.bookify.web.schedule;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import dev.popov.bookify.commons.DateTimeFactory;
import dev.popov.bookify.repository.EventRepository;

@Component
public class EventExpiryScheduler {
	private static final String EVERY_DAY = "* * */24 * * *";

	private final EventRepository eventRepository;
	private final DateTimeFactory dateTimeFactoy;

	@Autowired
	public EventExpiryScheduler(EventRepository eventRepository, DateTimeFactory dateTimeFactoy) {
		this.eventRepository = eventRepository;
		this.dateTimeFactoy = dateTimeFactoy;
	}

	@Transactional
	@Scheduled(cron = EVERY_DAY)
	public void removeExpiredEvents() {
		eventRepository.deleteAllByExpiresOnIsBefore(dateTimeFactoy.nowDate());
	}
}
