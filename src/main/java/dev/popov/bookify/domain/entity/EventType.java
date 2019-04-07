package dev.popov.bookify.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventType {
	FOOD("food"), ENTERTAINMENT("entertainment"), RELAX("relax"), SPORT("sport"), STUDY("study");

	private final String type;
}
