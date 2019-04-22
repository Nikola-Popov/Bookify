package unit.dev.popov.bookify.service.purchase;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import dev.popov.bookify.domain.entity.Purchase;
import dev.popov.bookify.domain.model.service.PurchaseServiceModel;
import dev.popov.bookify.repository.PurchaseRepository;
import dev.popov.bookify.service.purchase.PurchaseServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class PurchaseServiceTest {
	private static final String USERNAME = "username";
	private static final String ID = "id";

	@InjectMocks
	private PurchaseServiceImpl purchaseService;

	@Mock
	private ModelMapper modelMapperMock;

	@Mock
	private PurchaseRepository purchaseRepositoryMock;

	@Mock
	private PurchaseServiceModel purchaseServiceModelMock;

	@Mock
	private Purchase purchaseMock;

	@Before
	public void setUp() {
		when(modelMapperMock.map(purchaseServiceModelMock, Purchase.class)).thenReturn(purchaseMock);
		when(modelMapperMock.map(purchaseMock, PurchaseServiceModel.class)).thenReturn(purchaseServiceModelMock);
	}

	@Test
	public void testCreate() {
		purchaseService.create(purchaseServiceModelMock);

		verify(modelMapperMock).map(purchaseServiceModelMock, Purchase.class);
		verify(purchaseRepositoryMock).saveAndFlush(purchaseMock);
	}

	@Test
	public void testFindAll() {
		when(purchaseRepositoryMock.findAll()).thenReturn(asList(purchaseMock));

		assertThat(purchaseService.findAll(), equalTo(asList(purchaseServiceModelMock)));
	}

	@Test
	public void testFindAllByUsername() {
		when(purchaseRepositoryMock.findAllByUser_Username(USERNAME)).thenReturn(asList(purchaseMock));

		assertThat(purchaseService.findAllByUsername(USERNAME), equalTo(asList(purchaseServiceModelMock)));
	}

	@Test
	public void testDeleteById() {
		purchaseService.deleteById(ID);

		verify(purchaseRepositoryMock).deleteById(ID);
	}
}
