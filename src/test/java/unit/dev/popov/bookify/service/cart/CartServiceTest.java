package unit.dev.popov.bookify.service.cart;

import static java.math.BigDecimal.valueOf;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import dev.popov.bookify.domain.entity.Cart;
import dev.popov.bookify.domain.model.service.CartAddServiceModel;
import dev.popov.bookify.domain.model.service.CartServiceModel;
import dev.popov.bookify.domain.model.service.EventOrderServiceModel;
import dev.popov.bookify.domain.model.service.EventServiceModel;
import dev.popov.bookify.repository.CartRepository;
import dev.popov.bookify.service.cart.CartRetrievalService;
import dev.popov.bookify.service.cart.CartServiceImpl;
import dev.popov.bookify.service.cart.EventOrderServiceFactory;
import dev.popov.bookify.service.event.EventOrderService;
import dev.popov.bookify.service.event.EventService;

@RunWith(MockitoJUnitRunner.class)
public class CartServiceTest {
	private static final int QUANTITY = 2;
	private static final int EVENT_PRICE = 5;
	private static final String ID = "id";
	private static final String USERNAME = "username";
	private static final int TOTAL_PRICE = EVENT_PRICE * QUANTITY;

	@InjectMocks
	private CartServiceImpl cartService;

	@Mock
	private CartRepository cartRepositoryMock;

	@Mock
	private ModelMapper modelMapperMock;

	@Mock
	private EventService eventServiceMock;

	@Mock
	private CartRetrievalService cartRetrievalServiceMock;

	@Mock
	private EventOrderService eventOrderServiceMock;

	@Mock
	private CartServiceModel cartServiceModelMock;

	@Mock
	private EventOrderServiceFactory eventOrderServiceFactoryMock;

	@Mock
	private EventServiceModel eventServiceModelMock;

	@Mock
	private CartAddServiceModel cartAddServiceModelMock;

	@Mock
	private EventOrderServiceModel eventOrderServiceModelMock;

	@Mock
	private Cart cartMock;

	@Before
	public void setUp() {
		when(cartRetrievalServiceMock.retrieveCartByUsername(USERNAME)).thenReturn(cartServiceModelMock);

		when(cartAddServiceModelMock.getId()).thenReturn(ID);
		when(cartAddServiceModelMock.getUsername()).thenReturn(USERNAME);
		when(cartAddServiceModelMock.getQuantity()).thenReturn(QUANTITY);

		when(eventServiceMock.findById(ID)).thenReturn(eventServiceModelMock);

		when(eventOrderServiceFactoryMock.createEventOrderServiceModel(QUANTITY, eventServiceModelMock))
				.thenReturn(eventOrderServiceModelMock);

		when(modelMapperMock.map(cartServiceModelMock, Cart.class)).thenReturn(cartMock);
	}

	@Test
	public void testAdd() {
		when(cartServiceModelMock.getEvents()).thenReturn(new ArrayList<>());
		when(eventOrderServiceMock.create(eventOrderServiceModelMock)).thenReturn(createEventOrderServiceModel());

		cartService.add(cartAddServiceModelMock);

		verify(eventOrderServiceMock).create(eventOrderServiceModelMock);
		verify(cartServiceModelMock).setTotalPrice(valueOf(TOTAL_PRICE));
		verify(cartRepositoryMock).saveAndFlush(cartMock);
	}

	@Test
	public void testRetrieveCartRetrievesCartByUsername() {
		assertThat(cartService.retrieveCart(USERNAME), equalTo(cartServiceModelMock));
	}

	@Test
	public void testDeleteRemovesEventFromCart() {
		when(cartServiceModelMock.getEvents()).thenReturn(new ArrayList<>(asList(createEventOrderServiceModel())));

		cartService.delete(ID, USERNAME);

		verify(cartServiceModelMock).setTotalPrice(valueOf(0));
		verify(cartRepositoryMock).saveAndFlush(cartMock);
	}

	private EventOrderServiceModel createEventOrderServiceModel() {
		final EventServiceModel eventServiceModel = new EventServiceModel();
		eventServiceModel.setId(ID);
		eventServiceModel.setPrice(valueOf(EVENT_PRICE));

		return new EventOrderServiceModel(QUANTITY, eventServiceModel);
	}
}
