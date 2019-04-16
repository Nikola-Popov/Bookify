package dev.popov.bookify.commons.exceptions;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = NOT_FOUND, reason = "Unable to find user!")
public class UserNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UserNotFoundException() {
		super();
	}

	public UserNotFoundException(String message) {
		super(message);
	}
}
