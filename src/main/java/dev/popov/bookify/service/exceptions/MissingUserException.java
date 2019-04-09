package dev.popov.bookify.service.exceptions;

public class MissingUserException extends RuntimeException {

	public MissingUserException() {
	}

	public MissingUserException(String message) {
		super(message);
	}
}
