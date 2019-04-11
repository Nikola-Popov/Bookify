package dev.popov.bookify.service.user;

import static dev.popov.bookify.commons.constants.RoleConstants.ROLE_ROOT;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import dev.popov.bookify.commons.constants.RoleConstants;
import dev.popov.bookify.commons.exceptions.MissingUserException;
import dev.popov.bookify.domain.entity.Contact;
import dev.popov.bookify.domain.entity.Role;
import dev.popov.bookify.domain.entity.User;
import dev.popov.bookify.domain.model.service.ContactServiceModel;
import dev.popov.bookify.domain.model.service.RoleServiceModel;
import dev.popov.bookify.domain.model.service.UserServiceModel;
import dev.popov.bookify.repository.UserRepository;
import dev.popov.bookify.service.role.RoleService;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {
	private static final String MISSING_ID = "missingId";
	private static final String ID = "id";
	private static final String ENCODED_PASSWORD = "encodedPassword";
	private static final String PASSWORD = "password";
	private static final String USERNAME = "username";
	private static final String MISSING_USERNAME = "missingUsername";

	@InjectMocks
	private UserServiceImpl userServiceImpl;

	@Mock
	private UserRepository userRepositoryMock;

	@Mock
	private RoleService roleServiceMock;

	@Mock
	private ModelMapper modelMapperMock;

	@Mock
	private BCryptPasswordEncoder bCryptPasswordEncoderMock;

	@Mock
	private User userMock;

	@Mock
	private RoleServiceModel roleServiceModelMock;

	@Mock
	private UserServiceModel userServiceModelMock;

	@Mock
	private ContactServiceModel contactServiceModelMock;

	@Mock
	private Contact contactMock;

	@Before
	public void setUp() {
		when(modelMapperMock.map(userServiceModelMock, User.class)).thenReturn(userMock);
		when(userServiceModelMock.getPassword()).thenReturn(PASSWORD);
		when(bCryptPasswordEncoderMock.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
		when(roleServiceMock.findByAuthority(RoleConstants.ROLE_USER)).thenReturn(roleServiceModelMock);
		when(userServiceModelMock.getAuthorities()).thenReturn(new HashSet<>());
		when(userRepositoryMock.saveAndFlush(userMock)).thenReturn(userMock);
		when(modelMapperMock.map(userMock, UserServiceModel.class)).thenReturn(userServiceModelMock);
		when(userRepositoryMock.findById(ID)).thenReturn(Optional.of(userMock));
		when(modelMapperMock.map(contactServiceModelMock, Contact.class)).thenReturn(contactMock);
	}

	@Test(expected = UsernameNotFoundException.class)
	public void testLoadUserByUsernameThrowsExceptionBecauseNoUserFoundByUsername() {
		userServiceImpl.loadUserByUsername(MISSING_USERNAME);
	}

	@Test
	public void testLoadUserByUsername() {
		when(userRepositoryMock.findByUsername(USERNAME)).thenReturn(Optional.of(userMock));

		userServiceImpl.loadUserByUsername(USERNAME);

		verify(userRepositoryMock).findByUsername(USERNAME);
	}

	@Test
	public void testRegisterSetsAllRolesBecauseOfOriginUser() {
		when(userRepositoryMock.count()).thenReturn(0L);
		final Set<RoleServiceModel> roleServiceModels = createRolesServiceModels();
		when(roleServiceMock.findAllRoles()).thenReturn(roleServiceModels);

		userServiceImpl.register(userServiceModelMock);

		verify(userServiceModelMock).setAuthorities(roleServiceModels);
		verify(modelMapperMock).map(userServiceModelMock, User.class);
		verify(userMock).setPassword(ENCODED_PASSWORD);
		verify(userRepositoryMock).saveAndFlush(userMock);
		verify(modelMapperMock).map(userMock, UserServiceModel.class);
	}

	@Test
	public void testRegisterSetsUserRoleToEveryoneExceptOriginUser() {
		when(userRepositoryMock.count()).thenReturn(1L);

		userServiceImpl.register(userServiceModelMock);

		verify(modelMapperMock).map(userServiceModelMock, User.class);
		verify(userMock).setPassword(ENCODED_PASSWORD);
		verify(userRepositoryMock).saveAndFlush(userMock);
		verify(modelMapperMock).map(userMock, UserServiceModel.class);
	}

	@Test
	public void testFindAll() {
		when(userRepositoryMock.findAll()).thenReturn(asList(userMock));

		userServiceImpl.findAll();

		verify(userRepositoryMock).findAll();
		verify(modelMapperMock).map(userMock, UserServiceModel.class);
	}

	@Test(expected = MissingUserException.class)
	public void testEditThrowsExceptionBecauseOfMissingUserId() {
		userServiceImpl.edit(MISSING_ID, contactServiceModelMock);
	}

	@Test(expected = AccessDeniedException.class)
	public void testEditThrowsExceptionWhenEditingRoot() {
		when(userMock.getAuthorities()).thenReturn(createRootAuthority());

		userServiceImpl.edit(ID, contactServiceModelMock);
	}

	@Test
	public void testEditSuccessfullyEditsUserContantDetails() {
		userServiceImpl.edit(ID, contactServiceModelMock);

		verify(userMock).setContact(contactMock);
		verify(userRepositoryMock).saveAndFlush(userMock);
	}

	@Test(expected = MissingUserException.class)
	public void testDeleteThrowsExceptionBecauseOfMissingUserId() {
		userServiceImpl.delete(MISSING_ID);
	}

	@Test(expected = AccessDeniedException.class)
	public void testDeleteThrowsExceptionWhenDeletingRoot() {
		when(userMock.getAuthorities()).thenReturn(createRootAuthority());

		userServiceImpl.delete(ID);
	}

	@Test
	public void testDeleteSuccessfullyDeletesUser() {
		userServiceImpl.delete(ID);

		verify(userRepositoryMock).deleteById(ID);
	}

	private Set<Role> createRootAuthority() {
		return new HashSet<>(asList(new Role(ROLE_ROOT)));
	}

	private Set<RoleServiceModel> createRolesServiceModels() {
		final Set<RoleServiceModel> roleServiceModels = new HashSet<>();
		roleServiceModels.add(roleServiceModelMock);

		return roleServiceModels;
	}
}
