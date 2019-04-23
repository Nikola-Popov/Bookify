package unit.dev.popov.bookify.web.schedule;

import static java.time.LocalDate.of;
import static java.time.Month.JANUARY;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import dev.popov.bookify.commons.DateTimeFactory;
import dev.popov.bookify.repository.EventRepository;
import dev.popov.bookify.web.schedule.EventExpiryScheduler;

@RunWith(MockitoJUnitRunner.class)
public class EventExpirySchedulerTest {
	@InjectMocks
	private EventExpiryScheduler eventExpiryScheduler;

	@Mock
	private EventRepository eventRepositoryMock;

	@Mock
	private DateTimeFactory dateTimeFactoyMock;

	private LocalDate localDate = of(1111, JANUARY, 1);

	@Test
	public void testRemoveExpiredEvents() {
		when(dateTimeFactoyMock.nowDate()).thenReturn(localDate);

		eventExpiryScheduler.removeExpiredEvents();

		verify(eventRepositoryMock).deleteAllByExpiresOnIsBefore(localDate);
	}
}
