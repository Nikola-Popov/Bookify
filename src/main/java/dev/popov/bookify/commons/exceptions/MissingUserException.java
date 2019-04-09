package dev.popov.bookify.commons.exceptions;

public class MissingUserException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public MissingUserException() {
	}

	public MissingUserException(String message) {
		super(message);
	}
}
