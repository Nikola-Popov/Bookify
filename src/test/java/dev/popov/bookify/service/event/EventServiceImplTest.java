package dev.popov.bookify.service.event;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import dev.popov.bookify.domain.entity.Event;
import dev.popov.bookify.domain.model.service.EventServiceModel;
import dev.popov.bookify.repository.EventRepository;
import dev.popov.bookify.service.event.EventServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class EventServiceImplTest {
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

	@Test
	public void testCreatePersistsEventCorrectly() {
		when(modelMapperMock.map(eventServiceModelMock, Event.class)).thenReturn(eventMock);

		eventServiceImpl.create(eventServiceModelMock);

		verify(eventRepositoryMock).saveAndFlush(eventMock);
	}

	// TODO test findall
}
