package dev.popov.bookify.web.controllers;

import java.security.Principal;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import dev.popov.bookify.domain.model.binding.EventPurchaseBindingModel;
import dev.popov.bookify.domain.model.service.EventServiceModel;
import dev.popov.bookify.domain.model.service.PurchaseServiceModel;
import dev.popov.bookify.domain.model.service.UserServiceModel;
import dev.popov.bookify.service.purchase.PurchaseService;
import dev.popov.bookify.service.user.UserService;

@Controller("/purchases")
public class PurchaseController extends BaseController {
	private final ModelMapper modelMapper;
	private final PurchaseService purchaseService;
	private final UserService userService;

	@Autowired
	public PurchaseController(ModelMapper modelMapper, PurchaseService purchaseService, UserService userService) {
		this.modelMapper = modelMapper;
		this.purchaseService = purchaseService;
		this.userService = userService;
	}

	@PostMapping("/buy")
	public void purchase(
			@ModelAttribute(name = "eventPurchaseBindingModel") EventPurchaseBindingModel eventPurchaseBindingModel,
			Principal principal) {
		final EventServiceModel eventServiceModel = modelMapper.map(eventPurchaseBindingModel, EventServiceModel.class);
		final UserServiceModel userServiceModel = modelMapper.map(userService.loadUserByUsername(principal.getName()),
				UserServiceModel.class);

		PurchaseServiceModel purchaseServiceModel = new PurchaseServiceModel();
		purchaseServiceModel.setEventServiceModel(eventServiceModel);
		purchaseServiceModel.setUserServiceModel(userServiceModel);

		purchaseService.buy(purchaseServiceModel);
	}
}
