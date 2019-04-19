package dev.popov.bookify.domain.model.view;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CartViewModel extends BaseViewModel {
	private List<EventOrderViewModel> events = new ArrayList<>();

	private BigDecimal totalPrice = BigDecimal.ZERO;
}
