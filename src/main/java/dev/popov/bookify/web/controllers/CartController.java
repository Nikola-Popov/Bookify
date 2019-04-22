package dev.popov.bookify.web.controllers;

import static dev.popov.bookify.web.controllers.constants.common.AuthorizationConstants.IS_AUTHENTICATED;
import static dev.popov.bookify.web.controllers.constants.event.EventViewConstants.BROWSE_EVENTS;

import java.security.Principal;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import dev.popov.bookify.domain.model.binding.EventCartAddBindingModel;
import dev.popov.bookify.domain.model.service.CartAddServiceModel;
import dev.popov.bookify.domain.model.view.CartViewModel;
import dev.popov.bookify.service.cart.CartService;

@Controller
@RequestMapping("/cart")
public class CartController extends BaseController {
	private final CartService cartService;
	private final ModelMapper modelMapper;

	@Autowired
	public CartController(CartService cartService, ModelMapper modelMapper) {
		this.cartService = cartService;
		this.modelMapper = modelMapper;
	}

	@GetMapping
	@PreAuthorize(IS_AUTHENTICATED)
	public ModelAndView showCart(Principal principal, ModelAndView modelAndView) {
		final CartViewModel cart = modelMapper.map(cartService.retrieveCart(principal.getName()), CartViewModel.class);
		modelAndView.addObject("cart", cart);

		return view("cart/cart", modelAndView);
	}

	@PostMapping("/add")
	@PreAuthorize(IS_AUTHENTICATED)
	public ModelAndView add(Principal principal,
			@ModelAttribute(name = "eventCartAddBindingModel") EventCartAddBindingModel eventCartAddBindingModel) {
		final CartAddServiceModel cartAddServiceModel = new CartAddServiceModel();
		cartAddServiceModel.setUsername(principal.getName());
		cartAddServiceModel.setQuantity((eventCartAddBindingModel.getVouchersCount()));
		cartAddServiceModel.setId(eventCartAddBindingModel.getId());

		cartService.add(cartAddServiceModel);

		return view(BROWSE_EVENTS);
	}

	@DeleteMapping("/delete/{id}")
	@PreAuthorize(IS_AUTHENTICATED)
	public ModelAndView test(@PathVariable(name = "id") String id, Principal principal) {
		cartService.delete(id, principal.getName());

		return redirect("/cart");
	}

	@PostMapping("/checkout/{id}")
	public ModelAndView checkout(@PathVariable(name = "id") String id) {
		cartService.checkout(cartService.findById(id));

		return redirect("/users/profile/purchases");
	}
}
