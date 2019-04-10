package dev.popov.bookify.domain.model.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventTypeServiceModel {
	ALL("all"), FOOD("food"), ENTERTAINMENT("entertainment"), RELAX("relax"), SPORT("sport"), STUDY("study");

	private final String type;
}