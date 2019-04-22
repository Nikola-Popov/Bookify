package unit.dev.popov.bookify.validation;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.Errors;

import dev.popov.bookify.domain.entity.User;
import dev.popov.bookify.domain.model.binding.UserPasswordChangeBindingModel;
import dev.popov.bookify.repository.UserRepository;
import dev.popov.bookify.validation.UserPasswordValidator;
import dev.popov.bookify.validation.util.PasswordUtil;

@RunWith(MockitoJUnitRunner.class)
public class UserPasswordValidatorTest {
	private static final String CONFIRM_NEW_PASSWORD = "confirmNewPassword";
	private static final String NEW_PASSWORD = "newPassword";
	private static final String PASSWORD = "password";
	private static final String INVALID_CURRENT_PASSWORD = "Invalid current password";
	private static final String PASSWORD_AND_NEW_PASSWORD_ARE_EQUAL = "Password and new password are equal";
	private static final String NEW_PASSWORD_AND_CONFIRM_PASSWORD_DONT_MATCH = "New password and confirm password don`t match";
	private static final String ENCODED_PASSWORD = "encodedPassword";
	private static final String USERNAME = "username";

	@InjectMocks
	private UserPasswordValidator userPasswordValidator;

	@Mock
	private UserRepository userRepositoryMock;

	@Mock
	private BCryptPasswordEncoder bCryptPasswordEncoderMock;

	@Mock
	private PasswordUtil passwordUtilMock;

	@Mock
	private UserPasswordChangeBindingModel userPasswordChangeBindingModelMock;

	@Mock
	private User userMock;

	@Mock
	private Errors errorsMock;

	@Before
	public void setUp() {
		when(userPasswordChangeBindingModelMock.getUsername()).thenReturn(USERNAME);
		when(userRepositoryMock.findByUsername(USERNAME)).thenReturn(of(userMock));

		when(userMock.getPassword()).thenReturn(PASSWORD);
		when(userMock.getPassword()).thenReturn(ENCODED_PASSWORD);

		when(userPasswordChangeBindingModelMock.getPassword()).thenReturn(PASSWORD);
		when(userPasswordChangeBindingModelMock.getNewPassword()).thenReturn(NEW_PASSWORD);
		when(userPasswordChangeBindingModelMock.getConfirmNewPassword()).thenReturn(CONFIRM_NEW_PASSWORD);

		when(bCryptPasswordEncoderMock.matches(PASSWORD, ENCODED_PASSWORD)).thenReturn(true);
		when(passwordUtilMock.passwordsMatch(NEW_PASSWORD, CONFIRM_NEW_PASSWORD)).thenReturn(true);
	}

	@Test
	public void testSupports() {
		assertThat(userPasswordValidator.supports(UserPasswordChangeBindingModel.class), equalTo(true));
	}

	@Test(expected = UsernameNotFoundException.class)
	public void testValidateThrowExceptionWhenUserNotFound() {
		when(userRepositoryMock.findByUsername(USERNAME)).thenReturn(empty());

		userPasswordValidator.validate(userPasswordChangeBindingModelMock, errorsMock);
	}

	@Test
	public void testValidateRejectsWhenCurrentAndPersistedPasswordDontMatch() {
		when(bCryptPasswordEncoderMock.matches(PASSWORD, ENCODED_PASSWORD)).thenReturn(false);

		userPasswordValidator.validate(userPasswordChangeBindingModelMock, errorsMock);

		verify(errorsMock).rejectValue(PASSWORD, INVALID_CURRENT_PASSWORD, INVALID_CURRENT_PASSWORD);
	}

	@Test
	public void testValidateRejectsWhenNewAndConfirmNewPasswordDontMatch() {
		when(passwordUtilMock.passwordsMatch(NEW_PASSWORD, CONFIRM_NEW_PASSWORD)).thenReturn(false);

		userPasswordValidator.validate(userPasswordChangeBindingModelMock, errorsMock);

		verify(errorsMock).rejectValue(NEW_PASSWORD, NEW_PASSWORD_AND_CONFIRM_PASSWORD_DONT_MATCH,
				NEW_PASSWORD_AND_CONFIRM_PASSWORD_DONT_MATCH);
	}

	@Test
	public void testValidateRejectsWhenCurrentAndNewPasswordMatch() {
		when(userPasswordChangeBindingModelMock.getNewPassword()).thenReturn(PASSWORD);
		when(passwordUtilMock.passwordsMatch(PASSWORD, PASSWORD)).thenReturn(true);

		userPasswordValidator.validate(userPasswordChangeBindingModelMock, errorsMock);

		verify(errorsMock).rejectValue(PASSWORD, PASSWORD_AND_NEW_PASSWORD_ARE_EQUAL,
				PASSWORD_AND_NEW_PASSWORD_ARE_EQUAL);
	}

	@Test
	public void testValidateDontRejectWhenUserInputIsCorrect() {
		userPasswordValidator.validate(userPasswordChangeBindingModelMock, errorsMock);

		verify(errorsMock, never()).rejectValue(anyString(), anyString(), anyString());
	}
}
