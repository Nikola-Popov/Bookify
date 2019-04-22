package dev.popov.bookify.web.controllers;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
	private final PurchaseService purchaseService;
	private final ModelMapper modelMapper;

	@Autowired
	public PurchaseController(PurchaseService purchaseService, ModelMapper modelMapper) {
		this.purchaseService = purchaseService;
		this.modelMapper = modelMapper;
	}

	@GetMapping
	public ModelAndView purchases(ModelAndView modelAndView) {
		final List<PurchaseListViewModel> purchases = purchaseService.findAll().stream()
				.map(purchase -> modelMapper.map(purchase, PurchaseListViewModel.class)).collect(toList());
		modelAndView.addObject("purchases", purchases);

		return view("purchases/all_purchases", modelAndView);
	}

	@DeleteMapping("/delete/{id}")
	public ModelAndView delete(@PathVariable(name = "id") String id) {
		purchaseService.deleteById(id);

		return redirect("/purchases");
	}
}
