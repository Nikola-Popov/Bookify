package dev.popov.bookify.domain.model.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EventOrderViewModel extends BaseViewModel {
	private int quantity;

	private EventViewModel event;
}
