package dev.popov.bookify.web.factories;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import dev.popov.bookify.domain.entity.AuditLog;

public class AuditLogFactoryTest {
	private static final String USERNAME = "username";
	private static final String ACTION = "action";
	private AuditLogFactory auditLogFactory;

	@Before
	public void setUp() {
		auditLogFactory = new AuditLogFactory();
	}

	@Test
	public void testCreateAuditLog() {
		final AuditLog auditLog = auditLogFactory.createAuditLog(USERNAME, ACTION);

		assertThat(auditLog.getUsername(), equalTo(USERNAME));
		assertThat(auditLog.getAction(), equalTo(ACTION));
		assertThat(auditLog.getInvokedOn(), notNullValue());
	}
}
