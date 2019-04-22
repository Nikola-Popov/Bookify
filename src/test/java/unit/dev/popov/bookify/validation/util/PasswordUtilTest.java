package unit.dev.popov.bookify.validation.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import dev.popov.bookify.validation.util.PasswordUtil;

public class PasswordUtilTest {
	private static final String OTHER_PASSWORD = "otherPassword";
	private static final String PASSWORD = "password";

	private PasswordUtil passwordUtil;

	@Before
	public void setUp() {
		passwordUtil = new PasswordUtil();
	}

	@Test
	public void testPasswordMatchDifferentPasswordsReturnFalse() {
		assertThat(passwordUtil.passwordsMatch(PASSWORD, OTHER_PASSWORD), equalTo(false));
	}

	@Test
	public void testPasswordMatchSamePasswordsReturnTrue() {
		assertThat(passwordUtil.passwordsMatch(PASSWORD, PASSWORD), equalTo(true));
	}
}
