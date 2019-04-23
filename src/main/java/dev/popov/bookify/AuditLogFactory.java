package dev.popov.bookify;

import org.springframework.stereotype.Component;

import dev.popov.bookify.domain.entity.AuditLog;

@Component
public class AuditLogFactory {
	public AuditLog createAuditLog(String username, String action) {
		final AuditLog auditLog = new AuditLog();
		auditLog.setUsername(username);
		auditLog.setAction(action);

		return auditLog;
	}
}
