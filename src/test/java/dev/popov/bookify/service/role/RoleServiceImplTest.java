package dev.popov.bookify.service.role;

import static dev.popov.bookify.commons.constants.RoleConstants.ROLE_ADMIN;
import static dev.popov.bookify.commons.constants.RoleConstants.ROLE_ROOT;
import static dev.popov.bookify.commons.constants.RoleConstants.ROLE_USER;
import static java.util.Arrays.asList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import dev.popov.bookify.domain.entity.Role;
import dev.popov.bookify.domain.model.service.RoleServiceModel;
import dev.popov.bookify.repository.RoleRepository;

@RunWith(MockitoJUnitRunner.class)
public class RoleServiceImplTest {
	@InjectMocks
	private RoleServiceImpl roleServiceImpl;

	@Mock
	private RoleRepository roleRepositoryMock;

	@Mock
	private ModelMapper modelMapperMock;

	@Mock
	private RoleFactory roleFactoryMock;

	@Mock
	private Role roleUserMock;

	@Mock
	private Role roleAdminMock;

	@Mock
	private Role rolerRootMock;

	@Before
	public void setUp() {
		when(roleFactoryMock.createRole(ROLE_ADMIN)).thenReturn(roleAdminMock);
		when(roleFactoryMock.createRole(ROLE_USER)).thenReturn(roleUserMock);
		when(roleFactoryMock.createRole(ROLE_ROOT)).thenReturn(rolerRootMock);
	}

	@Test
	public void testSeedDefaultRolesInDbSeedsRolesCorrectlyBecauseOfOriginRun() {
		when(roleRepositoryMock.count()).thenReturn(0L);

		roleServiceImpl.seedDefaultRolesInDb();

		verify(roleRepositoryMock).saveAndFlush(roleAdminMock);
		verify(roleRepositoryMock).saveAndFlush(roleUserMock);
		verify(roleRepositoryMock).saveAndFlush(rolerRootMock);
	}

	@Test
	public void testSeedDefaultRolesInDbDoesNothingAfterOriginRun() {
		when(roleRepositoryMock.count()).thenReturn(1L);

		roleServiceImpl.seedDefaultRolesInDb();

		verify(roleRepositoryMock, never()).saveAndFlush(any(Role.class));
	}

	@Test
	public void testFindAllRoles() {
		when(roleRepositoryMock.findAll()).thenReturn(asList(roleUserMock));

		roleServiceImpl.findAllRoles();

		verify(roleRepositoryMock).findAll();
		verify(modelMapperMock).map(roleUserMock, RoleServiceModel.class);
	}

	@Test
	public void testFindByAuthorityFindsTheCorrectRole() {
		when(roleRepositoryMock.findByAuthority(ROLE_USER)).thenReturn(roleUserMock);

		roleServiceImpl.findByAuthority(ROLE_USER);

		verify(roleRepositoryMock).findByAuthority(ROLE_USER);
		verify(modelMapperMock).map(roleUserMock, RoleServiceModel.class);
	}
}
