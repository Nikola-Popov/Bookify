package unit.dev.popov.bookify.service.cart;

import static java.util.Optional.empty;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;

import dev.popov.bookify.domain.entity.Cart;
import dev.popov.bookify.domain.entity.User;
import dev.popov.bookify.domain.model.service.CartServiceModel;
import dev.popov.bookify.repository.CartRepository;
import dev.popov.bookify.service.cart.CartRetrievalServiceImpl;
import dev.popov.bookify.service.user.UserService;

@RunWith(MockitoJUnitRunner.class)
public class CartRetrievalServiceTest {
	private static final String USERNAME = "username";

	@InjectMocks
	private CartRetrievalServiceImpl cartRetrievalService;

	@Mock
	private CartRepository cartRepositoryMock;

	@Mock
	private ModelMapper modelMapperMock;

	@Mock
	private UserService userServiceMock;

	@Mock
	private CartServiceModel cartServiceModelMock;

	@Mock
	private Cart cartMock;

	@Mock
	private UserDetails userDetailsMock;

	@Mock
	private User userMock;

	@Before
	public void setUp() {
		when(userServiceMock.loadUserByUsername(USERNAME)).thenReturn(userDetailsMock);
		when(modelMapperMock.map(userDetailsMock, User.class)).thenReturn(userMock);

		when(modelMapperMock.map(any(Cart.class), eq(CartServiceModel.class))).thenReturn(cartServiceModelMock);
	}

	@Test
	public void testRetrieveCartByUsernameCreatesNewCartAndSetsUserWhenUserDoesntHaveCart() {
		when(cartRepositoryMock.findByUser_Username(USERNAME)).thenReturn(empty());

		assertThat(cartRetrievalService.retrieveCartByUsername(USERNAME), equalTo(cartServiceModelMock));
	}

	@Test
	public void testRetrieveCartByUsernameRetrievesCartForUser() {
		when(cartRepositoryMock.findByUser_Username(USERNAME)).thenReturn(Optional.of(cartMock));
		when(cartMock.getUser()).thenReturn(userMock);

		assertThat(cartRetrievalService.retrieveCartByUsername(USERNAME), equalTo(cartServiceModelMock));
	}
}
