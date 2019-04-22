package dev.popov.bookify.service.purchase;

import java.util.List;

import dev.popov.bookify.domain.model.service.PurchaseServiceModel;

public interface PurchaseService {
	void create(PurchaseServiceModel purchaseServiceModel);

	List<PurchaseServiceModel> findAll();

	List<PurchaseServiceModel> findAllByUsername(String username);

	void deleteById(String id);
}
