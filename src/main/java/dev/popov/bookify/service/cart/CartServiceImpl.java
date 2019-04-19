package dev.popov.bookify.service.cart;

import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.popov.bookify.domain.entity.Cart;
import dev.popov.bookify.domain.entity.EventOrder;
import dev.popov.bookify.domain.entity.User;
import dev.popov.bookify.domain.model.service.CartAddServiceModel;
import dev.popov.bookify.domain.model.service.CartServiceModel;
import dev.popov.bookify.domain.model.service.EventOrderServiceModel;
import dev.popov.bookify.domain.model.service.EventServiceModel;
import dev.popov.bookify.repository.CartRepository;
import dev.popov.bookify.service.event.EventOrderService;
import dev.popov.bookify.service.event.EventService;
import dev.popov.bookify.service.user.UserService;

@Service
public class CartServiceImpl implements CartService {
	private final CartRepository cartRepository;
	private final ModelMapper modelMapper;
	private final EventService eventService;
	private final UserService userService;
	private final EventOrderService eventOrderService;

	@Autowired
	public CartServiceImpl(UserService userService, EventService eventService, CartRepository cartRepository,
			ModelMapper modelMapper, EventOrderService eventOrderService) {
		this.cartRepository = cartRepository;
		this.modelMapper = modelMapper;
		this.eventService = eventService;
		this.userService = userService;
		this.eventOrderService = eventOrderService;
	}

	@Override
	public void add(CartAddServiceModel cartServiceModel) {
		final Cart cart = retrieveCartByUsername(cartServiceModel.getUsername());

		final EventOrderServiceModel eventOrderServiceModel = new EventOrderServiceModel();
		eventOrderServiceModel.setQuantity(cartServiceModel.getQuantity());
		eventOrderServiceModel
				.setEvent(modelMapper.map(eventService.findById(cartServiceModel.getId()), EventServiceModel.class));

		cart.getEvents().add(modelMapper.map(eventOrderService.create(eventOrderServiceModel), EventOrder.class));
		cart.setTotalPrice(calculateTotalPrice(cart.getEvents()));

		cartRepository.saveAndFlush(cart);
	}

	@Override
	public CartServiceModel retrieveCart(String username) {
		return modelMapper.map(retrieveCartByUsername(username), CartServiceModel.class);
	}

	private Cart retrieveCartByUsername(String username) {
		final Cart cart = cartRepository.findByUser_Username(username).orElseGet(Cart::new);
		if (cart.getUser() == null) {
			cart.setUser(modelMapper.map(userService.loadUserByUsername(username), User.class));
		}
		return cart;
	}

	private BigDecimal calculateTotalPrice(List<EventOrder> eventOrders) {
		return eventOrders.stream().filter(Objects::nonNull)
				.map(eventOrder -> eventOrder.getEvent().getPrice().multiply(valueOf(eventOrder.getQuantity())))
				.reduce(ZERO, BigDecimal::add);
	}

	@Override
	public void delete(String id, String username) {
		final Cart cart = retrieveCartByUsername(username);
		cart.getEvents().removeIf(eventOrder -> eventOrder.getEvent().getId().equals(id));
		cart.setTotalPrice(calculateTotalPrice(cart.getEvents()));

		cartRepository.saveAndFlush(cart);
	}
}
