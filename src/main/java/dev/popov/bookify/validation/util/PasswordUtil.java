package dev.popov.bookify.validation.util;

import org.springframework.stereotype.Component;

@Component
public class PasswordUtil {
	public boolean passwordsMatch(String password, String otherPassword) {
		return password != null && password.equals(otherPassword);
	}
}
