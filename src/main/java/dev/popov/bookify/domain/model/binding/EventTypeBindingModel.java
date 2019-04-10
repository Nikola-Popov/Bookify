package dev.popov.bookify.domain.model.binding;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventTypeBindingModel {
	FOOD("food"), ENTERTAINMENT("entertainment"), RELAX("relax"), SPORT("sport"), STUDY("study");

	private final String type;
}
