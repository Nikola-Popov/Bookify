package dev.popov.bookify.domain.model.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CartAddServiceModel extends BaseServiceModel {
	private String username;
	private int quantity;
}
