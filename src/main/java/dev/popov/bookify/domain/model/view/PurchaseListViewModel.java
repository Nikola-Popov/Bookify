package dev.popov.bookify.domain.model.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PurchaseListViewModel extends BaseViewModel {
	private UserViewModel user;
	private EventOrderViewModel event;
}
