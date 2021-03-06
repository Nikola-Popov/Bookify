package unit.dev.popov.bookify.service.event;

import static dev.popov.bookify.domain.model.service.EventTypeServiceModel.ALL;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import dev.popov.bookify.commons.exceptions.EventNotFoundException;
import dev.popov.bookify.domain.entity.Event;
import dev.popov.bookify.domain.entity.EventType;
import dev.popov.bookify.domain.model.service.EventServiceModel;
import dev.popov.bookify.domain.model.service.EventTypeServiceModel;
import dev.popov.bookify.repository.EventRepository;
import dev.popov.bookify.service.event.EventServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class EventServiceTest {
	private static final String MISSING_ID = "missingId";
	private static final String ID = "id";

	@InjectMocks
	private EventServiceImpl eventServiceImpl;

	@Mock
	private EventRepository eventRepositoryMock;

	@Mock
	private ModelMapper modelMapperMock;

	@Mock
	private EventServiceModel eventServiceModelMock;

	@Mock
	private Event eventMock;

	@Before
	public void setUp() {
		when(modelMapperMock.map(eventServiceModelMock, Event.class)).thenReturn(eventMock);
		when(modelMapperMock.map(eventMock, EventServiceModel.class)).thenReturn(eventServiceModelMock);
		when(eventRepositoryMock.findAll()).thenReturn(asList(eventMock));
		when(eventRepositoryMock.findById(ID)).thenReturn(Optional.of(eventMock));
	}

	@Test
	public void testFindAll() {
		eventServiceImpl.findAll();

		verify(eventRepositoryMock).findAll();
		verify(modelMapperMock).map(eventMock, EventServiceModel.class);
	}

	@Test
	public void testCreatePersistsEventCorrectly() {
		eventServiceImpl.create(eventServiceModelMock);

		verify(eventRepositoryMock).saveAndFlush(eventMock);
		verify(modelMapperMock).map(eventServiceModelMock, Event.class);
	}

	@Test
	public void testFindAllByEventTypeReturnsAllEventsBecauseOfAllEventType() {
		eventServiceImpl.findAllByEventType(ALL);

		verify(eventRepositoryMock).findAll();
		verify(modelMapperMock).map(eventMock, EventServiceModel.class);
	}

	@Test
	public void testFindAllByEventTypeReturnsMatchingEvents() {
		when(modelMapperMock.map(EventTypeServiceModel.ENTERTAINMENT, EventType.class))
				.thenReturn(EventType.ENTERTAINMENT);
		when(eventRepositoryMock.findAllByEventType(EventType.ENTERTAINMENT)).thenReturn(asList(eventMock));

		eventServiceImpl.findAllByEventType(EventTypeServiceModel.ENTERTAINMENT);

		verify(eventRepositoryMock).findAllByEventType(EventType.ENTERTAINMENT);
	}

	@Test
	public void testDeleteSuccessfullyRemovesEvent() {
		eventServiceImpl.delete(ID);

		verify(eventRepositoryMock).deleteById(ID);
	}

	@Test(expected = EventNotFoundException.class)
	public void testEditThrowsExceptionBecauseNoEventIsFound() {
		eventServiceImpl.edit(MISSING_ID, eventServiceModelMock);
	}

	@Test
	public void testEditSuccessfullyEditsEvent() {
		eventServiceImpl.edit(ID, eventServiceModelMock);

		verify(eventRepositoryMock).findById(ID);
		verify(eventServiceModelMock).setId(ID);
		verify(eventRepositoryMock).saveAndFlush(eventMock);
	}

	@Test(expected = EventNotFoundException.class)
	public void testFindByIdThrowsExceptionWhenSuchEventMisses() {
		eventServiceImpl.findById(MISSING_ID);
	}

	@Test
	public void testFindByIdReturnsCorrectEvent() {
		assertThat(eventServiceImpl.findById(ID), equalTo(eventServiceModelMock));
	}
}
