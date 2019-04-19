package dev.popov.bookify.service.cart;

import dev.popov.bookify.domain.model.service.CartAddServiceModel;
import dev.popov.bookify.domain.model.service.CartServiceModel;

public interface CartService {
	void add(CartAddServiceModel cartServiceModel);

	CartServiceModel retrieveCart(String username);

	void delete(String id, String username);
}
