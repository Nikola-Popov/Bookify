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
import dev.popov.bookify.domain.model.service.CartAddServiceModel;
import dev.popov.bookify.domain.model.service.CartServiceModel;
import dev.popov.bookify.domain.model.service.EventOrderServiceModel;
import dev.popov.bookify.repository.CartRepository;
import dev.popov.bookify.service.event.EventOrderService;
import dev.popov.bookify.service.event.EventService;

@Service
public class CartServiceImpl implements CartService {
	private final CartRepository cartRepository;
	private final ModelMapper modelMapper;
	private final EventService eventService;
	private final EventOrderService eventOrderService;
	private final EventOrderServiceFactory eventOrderServiceFactory;
	private final CartRetrievalService cartRetrievalService;

	@Autowired
	public CartServiceImpl(EventService eventService, CartRepository cartRepository, ModelMapper modelMapper,
			EventOrderService eventOrderService, EventOrderServiceFactory eventOrderServiceFactory,
			CartRetrievalService cartRetrievalService) {
		this.cartRepository = cartRepository;
		this.modelMapper = modelMapper;
		this.eventService = eventService;
		this.eventOrderService = eventOrderService;
		this.eventOrderServiceFactory = eventOrderServiceFactory;
		this.cartRetrievalService = cartRetrievalService;
	}

	@Override
	public void add(CartAddServiceModel cartAddServiceModel) {
		final CartServiceModel cartServiceModel = cartRetrievalService
				.retrieveCartByUsername(cartAddServiceModel.getUsername());

		final EventOrderServiceModel eventOrderServiceModel = eventOrderServiceFactory.createEventOrderServiceModel(
				cartAddServiceModel.getQuantity(), eventService.findById(cartAddServiceModel.getId()));

		cartServiceModel.getEvents().add(eventOrderService.create(eventOrderServiceModel));
		cartServiceModel.setTotalPrice(calculateTotalPrice(cartServiceModel.getEvents()));

		cartRepository.saveAndFlush(modelMapper.map(cartServiceModel, Cart.class));
	}

	@Override
	public CartServiceModel retrieveCart(String username) {
		return cartRetrievalService.retrieveCartByUsername(username);
	}

	@Override
	public void delete(String id, String username) {
		final CartServiceModel cartServiceModel = cartRetrievalService.retrieveCartByUsername(username);
		cartServiceModel.getEvents().removeIf(eventOrder -> eventOrder.getEvent().getId().equals(id));
		cartServiceModel.setTotalPrice(calculateTotalPrice(cartServiceModel.getEvents()));

		cartRepository.saveAndFlush(modelMapper.map(cartServiceModel, Cart.class));
	}

	private BigDecimal calculateTotalPrice(List<EventOrderServiceModel> eventOrders) {
		return eventOrders.stream().filter(Objects::nonNull)
				.map(eventOrder -> eventOrder.getEvent().getPrice().multiply(valueOf(eventOrder.getQuantity())))
				.reduce(ZERO, BigDecimal::add);
	}
}
