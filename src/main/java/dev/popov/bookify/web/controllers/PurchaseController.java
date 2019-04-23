package dev.popov.bookify.web.controllers;

import static dev.popov.bookify.web.controllers.constants.common.AuthorizationConstants.HAS_ADMIN_ROLE;
import static dev.popov.bookify.web.controllers.constants.purchase.PurchasePathConstants.DELETE;
import static dev.popov.bookify.web.controllers.constants.purchase.PurchasePathConstants.PURCHASES;
import static dev.popov.bookify.web.controllers.constants.purchase.PurchaseViewConstants.PURCHASES_ALL_PURCHASES;
import static java.util.stream.Collectors.toList;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import dev.popov.bookify.domain.model.view.PurchaseListViewModel;
import dev.popov.bookify.service.purchase.PurchaseService;

@Controller
@RequestMapping("/purchases")
public class PurchaseController extends BaseController {
	private static final String PURCHASES_OBJECT = "purchases";

	private final PurchaseService purchaseService;
	private final ModelMapper modelMapper;

	@Autowired
	public PurchaseController(PurchaseService purchaseService, ModelMapper modelMapper) {
		this.purchaseService = purchaseService;
		this.modelMapper = modelMapper;
	}

	@GetMapping
	@PreAuthorize(HAS_ADMIN_ROLE)
	public ModelAndView purchases(ModelAndView modelAndView) {
		final List<PurchaseListViewModel> purchases = purchaseService.findAll().stream()
				.map(purchase -> modelMapper.map(purchase, PurchaseListViewModel.class)).collect(toList());
		modelAndView.addObject(PURCHASES_OBJECT, purchases);

		return view(PURCHASES_ALL_PURCHASES, modelAndView);
	}

	@DeleteMapping(DELETE + "/{id}")
	@PreAuthorize(HAS_ADMIN_ROLE)
	public ModelAndView delete(@PathVariable(name = "id") String id) {
		purchaseService.deleteById(id);

		return redirect(PURCHASES);
	}
}
