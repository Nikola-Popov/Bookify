package unit.dev.popov.bookify.service.event;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import dev.popov.bookify.domain.entity.EventOrder;
import dev.popov.bookify.domain.model.service.EventOrderServiceModel;
import dev.popov.bookify.repository.EventOrderRepository;
import dev.popov.bookify.service.event.EventOrderServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class EventOrderServiceTest {
	@InjectMocks
	private EventOrderServiceImpl eventOrderService;

	@Mock
	private EventOrderRepository eventOrderRepositoryMock;

	@Mock
	private ModelMapper modelMapperMock;

	@Mock
	private EventOrder eventOrderMock;

	@Mock
	private EventOrderServiceModel eventOrderServiceModelMock;

	@Before
	public void setUp() {
		when(modelMapperMock.map(eventOrderServiceModelMock, EventOrder.class)).thenReturn(eventOrderMock);
		when(modelMapperMock.map(eventOrderMock, EventOrderServiceModel.class)).thenReturn(eventOrderServiceModelMock);
		when(eventOrderRepositoryMock.saveAndFlush(eventOrderMock)).thenReturn(eventOrderMock);
	}

	@Test
	public void testCreateCreatesCorrectEventOrderServiceModel() {
		assertThat(eventOrderService.create(eventOrderServiceModelMock), equalTo(eventOrderServiceModelMock));
	}
}
