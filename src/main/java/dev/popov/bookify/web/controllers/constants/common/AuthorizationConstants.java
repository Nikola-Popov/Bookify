package dev.popov.bookify.web.controllers.constants.common;

import static dev.popov.bookify.commons.constants.RoleConstants.ROLE_ADMIN;

public final class AuthorizationConstants {
	public static final String IS_ANONYMOUS = "isAnonymous()";
	public static final String IS_AUTHENTICATED = "isAuthenticated()";
	public static final String HAS_ADMIN_ROLE = "hasRole('" + ROLE_ADMIN + "')";
}
