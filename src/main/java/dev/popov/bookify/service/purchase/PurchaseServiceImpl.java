package dev.popov.bookify.service.purchase;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.popov.bookify.domain.entity.Purchase;
import dev.popov.bookify.domain.model.service.PurchaseServiceModel;
import dev.popov.bookify.repository.PurchaseRepository;

@Service
public class PurchaseServiceImpl implements PurchaseService {
	private final PurchaseRepository purchaseRepository;
	private final ModelMapper modelMapper;

	@Autowired
	public PurchaseServiceImpl(PurchaseRepository purchaseRepository, ModelMapper modelMapper) {
		this.purchaseRepository = purchaseRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public void buy(PurchaseServiceModel purchaseServiceModel) {
		purchaseRepository.saveAndFlush(modelMapper.map(purchaseServiceModel, Purchase.class));
	}
}
