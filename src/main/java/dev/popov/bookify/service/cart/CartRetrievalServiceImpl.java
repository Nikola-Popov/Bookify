package dev.popov.bookify.service.cart;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.popov.bookify.domain.entity.Cart;
import dev.popov.bookify.domain.entity.User;
import dev.popov.bookify.domain.model.service.CartServiceModel;
import dev.popov.bookify.repository.CartRepository;
import dev.popov.bookify.service.user.UserService;

@Service
public class CartRetrievalServiceImpl implements CartRetrievalService {
	private final CartRepository cartRepository;
	private final ModelMapper modelMapper;
	private final UserService userService;

	@Autowired
	public CartRetrievalServiceImpl(CartRepository cartRepository, ModelMapper modelMapper, UserService userService) {
		this.cartRepository = cartRepository;
		this.modelMapper = modelMapper;
		this.userService = userService;
	}

	@Override
	public CartServiceModel retrieveCartByUsername(String username) {
		final Cart cart = cartRepository.findByUser_Username(username).orElseGet(Cart::new);
		if (cart.getUser() == null) {
			cart.setUser(modelMapper.map(userService.loadUserByUsername(username), User.class));
		}
		return modelMapper.map(cart, CartServiceModel.class);
	}
}
