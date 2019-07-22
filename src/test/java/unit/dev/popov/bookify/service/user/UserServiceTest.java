package unit.dev.popov.bookify.service.user;

import static dev.popov.bookify.commons.constants.RoleConstants.ROLE_ROOT;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import dev.popov.bookify.commons.constants.RoleConstants;
import dev.popov.bookify.commons.exceptions.ForbiddenActionOnRootException;
import dev.popov.bookify.commons.exceptions.UserNotFoundException;
import dev.popov.bookify.domain.entity.Contact;
import dev.popov.bookify.domain.entity.Role;
import dev.popov.bookify.domain.entity.User;
import dev.popov.bookify.domain.model.service.ContactServiceModel;
import dev.popov.bookify.domain.model.service.RoleServiceModel;
import dev.popov.bookify.domain.model.service.UserEditServiceModel;
import dev.popov.bookify.domain.model.service.UserServiceModel;
import dev.popov.bookify.repository.UserRepository;
import dev.popov.bookify.service.cloud.CloudinaryService;
import dev.popov.bookify.service.role.RoleService;
import dev.popov.bookify.service.user.UserServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
	private static final String IMAGE_URL = "imageUrl";
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
	private PasswordEncoder passwordEncoderMock;

	@Mock
	private User userMock;

	@Mock
	private RoleServiceModel roleServiceModelMock;

	@Mock
	private UserServiceModel userServiceModelMock;

	@Mock
	private UserEditServiceModel userEditServiceModelMock;

	@Mock
	private ContactServiceModel contactServiceModelMock;

	@Mock
	private Contact contactMock;

	@Mock
	private CloudinaryService cloudinaryServiceMock;

	@Mock
	private MultipartFile multipartFileMock;

	@Before
	public void setUp() throws Exception {
		when(modelMapperMock.map(userServiceModelMock, User.class)).thenReturn(userMock);
		when(modelMapperMock.map(userMock, UserServiceModel.class)).thenReturn(userServiceModelMock);
		when(modelMapperMock.map(contactServiceModelMock, Contact.class)).thenReturn(contactMock);

		when(userServiceModelMock.getPassword()).thenReturn(PASSWORD);
		when(userServiceModelMock.getAuthorities()).thenReturn(new HashSet<>());

		when(userEditServiceModelMock.getUsername()).thenReturn(USERNAME);
		when(userEditServiceModelMock.getContact()).thenReturn(contactServiceModelMock);
		when(userEditServiceModelMock.getImage()).thenReturn(multipartFileMock);
		when(userEditServiceModelMock.getPassword()).thenReturn(PASSWORD);

		when(passwordEncoderMock.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);

		when(roleServiceMock.findByAuthority(RoleConstants.ROLE_USER)).thenReturn(roleServiceModelMock);

		when(userRepositoryMock.saveAndFlush(userMock)).thenReturn(userMock);
		when(userRepositoryMock.findById(ID)).thenReturn(Optional.of(userMock));

		when(userMock.getContact()).thenReturn(contactMock);

		when(contactMock.getId()).thenReturn(ID);

		when(cloudinaryServiceMock.uploadImage(multipartFileMock)).thenReturn(IMAGE_URL);
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

	@Test(expected = UserNotFoundException.class)
	public void testEditThrowsExceptionBecauseOfMissingUserId() throws IOException {
		userServiceImpl.edit(MISSING_ID, userEditServiceModelMock);
	}

	@Test(expected = ForbiddenActionOnRootException.class)
	public void testEditThrowsExceptionWhenEditingRoot() throws IOException {
		when(userMock.getAuthorities()).thenReturn(createRootAuthority());

		userServiceImpl.edit(ID, userEditServiceModelMock);
	}

	@Test
	public void testEditSuccessfullyEditsUserContantDetails() throws IOException {
		userServiceImpl.edit(ID, userEditServiceModelMock);

		verify(userMock).setUsername(USERNAME);
		verify(userMock).setContact(contactMock);
		verify(contactServiceModelMock).setId(ID);
		verify(userMock).setPassword(ENCODED_PASSWORD);
		verify(userMock).setImage(IMAGE_URL);
		verify(userMock).setContact(contactMock);
		verify(userRepositoryMock).saveAndFlush(userMock);
	}

	@Test(expected = UserNotFoundException.class)
	public void testDeleteThrowsExceptionBecauseOfMissingUserId() {
		userServiceImpl.delete(MISSING_ID);
	}

	@Test(expected = ForbiddenActionOnRootException.class)
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
