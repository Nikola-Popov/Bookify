package dev.popov.bookify.service.role;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class RoleFactoryTest {
	private static final String AUTHORITY = "authority";

	@Test
	public void testCreateRole() {
		assertThat(new RoleFactory().createRole(AUTHORITY).getAuthority(), equalTo(AUTHORITY));
	}
}
