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
import org.springframework.validation.Errors;

import dev.popov.bookify.domain.entity.User;
import dev.popov.bookify.domain.model.binding.ContactRegisterBindingModel;
import dev.popov.bookify.domain.model.binding.UserRegisterBindingModel;
import dev.popov.bookify.repository.UserRepository;
import dev.popov.bookify.validation.UserRegisterValidator;
import dev.popov.bookify.validation.util.PasswordUtil;

@RunWith(MockitoJUnitRunner.class)
public class UserRegisterValidatorTest {
	private static final String EMAIL = "email";
	private static final String PASSWORD = "password";
	private static final String EMAIL_MUST_NOT_BE_BLANK = "Email must not be blank";
	private static final String USERNAME = "username";
	private static final String USERNAME_IS_ALREADY_TAKEN = "Username is already taken";
	private static final String PASSWORDS_DO_NOT_MATCH = "Passwords do not match";
	private static final String CONFIRM_PASSWORD = "confirmPassword";

	@InjectMocks
	private UserRegisterValidator userRegisterValidator;

	@Mock
	private UserRepository userRepositoryMock;

	@Mock
	private PasswordUtil passwordUtilMock;

	@Mock
	private UserRegisterBindingModel userRegisterBindingModelMock;

	@Mock
	private Errors errorsMock;

	@Mock
	private User userMock;

	@Mock
	private ContactRegisterBindingModel contactRegisterBindingModelMock;

	@Before
	public void setUp() {
		when(userRegisterBindingModelMock.getUsername()).thenReturn(USERNAME);
		when(userRepositoryMock.findByUsername(USERNAME)).thenReturn(empty());

		when(userRegisterBindingModelMock.getPassword()).thenReturn(PASSWORD);
		when(userRegisterBindingModelMock.getConfirmPassword()).thenReturn(CONFIRM_PASSWORD);
		when(passwordUtilMock.passwordsMatch(PASSWORD, CONFIRM_PASSWORD)).thenReturn(true);

		when(userRegisterBindingModelMock.getContact()).thenReturn(contactRegisterBindingModelMock);
		when(contactRegisterBindingModelMock.getEmail()).thenReturn(EMAIL);
	}

	@Test
	public void testSupports() {
		assertThat(userRegisterValidator.supports(UserRegisterBindingModel.class), equalTo(true));
	}

	@Test
	public void testValidateRejectsWhenUserWithSameUsernameExists() {
		when(userRepositoryMock.findByUsername(USERNAME)).thenReturn(of(userMock));

		userRegisterValidator.validate(userRegisterBindingModelMock, errorsMock);

		verify(errorsMock).rejectValue(USERNAME, USERNAME_IS_ALREADY_TAKEN, USERNAME_IS_ALREADY_TAKEN);
	}

	@Test
	public void testValidateRejectWhenPasswordAndConfirmPasswordDontMatch() {
		when(passwordUtilMock.passwordsMatch(PASSWORD, CONFIRM_PASSWORD)).thenReturn(false);

		userRegisterValidator.validate(userRegisterBindingModelMock, errorsMock);

		verify(errorsMock).rejectValue(CONFIRM_PASSWORD, PASSWORDS_DO_NOT_MATCH, PASSWORDS_DO_NOT_MATCH);
	}

	@Test
	public void testValidateRejectsWhenUserContactIsNull() {
		when(userRegisterBindingModelMock.getContact()).thenReturn(null);

		userRegisterValidator.validate(userRegisterBindingModelMock, errorsMock);

		verify(errorsMock).rejectValue(EMAIL, EMAIL_MUST_NOT_BE_BLANK, EMAIL_MUST_NOT_BE_BLANK);
	}

	@Test
	public void testValidateRejectsWhenUserContactEmailIsBlank() {
		when(contactRegisterBindingModelMock.getEmail()).thenReturn(null);

		userRegisterValidator.validate(userRegisterBindingModelMock, errorsMock);

		verify(errorsMock).rejectValue(EMAIL, EMAIL_MUST_NOT_BE_BLANK, EMAIL_MUST_NOT_BE_BLANK);
	}

	@Test
	public void testValidateDontRejectWhenUserInputIsCorrect() {
		userRegisterValidator.validate(userRegisterBindingModelMock, errorsMock);

		verify(errorsMock, never()).rejectValue(anyString(), anyString(), anyString());
	}
}
