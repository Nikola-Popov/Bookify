package dev.popov.bookify.domain.model.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PurchaseServiceModel {
	private EventServiceModel eventServiceModel;
	private UserServiceModel userServiceModel;
}
