package dev.popov.bookify.commons.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ForbiddenActionOnRootException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ForbiddenActionOnRootException(String message) {
		super(message);
	}

}
