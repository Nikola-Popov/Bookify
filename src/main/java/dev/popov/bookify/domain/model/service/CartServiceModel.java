package dev.popov.bookify.domain.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CartServiceModel extends BaseServiceModel {
	@NotNull
	private UserServiceModel user;

	private List<EventOrderServiceModel> events = new ArrayList<>();

	private BigDecimal totalPrice;
}
