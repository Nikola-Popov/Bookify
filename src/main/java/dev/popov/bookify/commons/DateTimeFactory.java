package dev.popov.bookify.commons;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

@Component
public class DateTimeFactory {
	public LocalDate nowDate() {
		return LocalDate.now();
	}
}
