package dev.popov.bookify.domain.model.view;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventTypeViewModel {
	FOOD("food"), ENTERTAINMENT("entertainment"), RELAX("relax"), SPORT("sport"), STUDY("study");

	private final String type;
}
