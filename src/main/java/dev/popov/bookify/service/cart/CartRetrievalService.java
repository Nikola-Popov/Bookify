package dev.popov.bookify.service.cart;

import dev.popov.bookify.domain.model.service.CartServiceModel;

public interface CartRetrievalService {
	CartServiceModel retrieveCartByUsername(String username);
}
