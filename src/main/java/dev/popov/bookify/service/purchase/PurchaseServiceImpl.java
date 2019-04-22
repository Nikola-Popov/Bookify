package dev.popov.bookify.service.purchase;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.popov.bookify.domain.entity.Purchase;
import dev.popov.bookify.domain.model.service.PurchaseServiceModel;
import dev.popov.bookify.repository.PurchaseRepository;

@Service
public class PurchaseServiceImpl implements PurchaseService {
	private final ModelMapper modelMapper;
	private final PurchaseRepository purchaseRepository;

	@Autowired
	public PurchaseServiceImpl(ModelMapper modelMapper, PurchaseRepository purchaseRepository) {
		this.modelMapper = modelMapper;
		this.purchaseRepository = purchaseRepository;
	}

	@Override
	public void create(PurchaseServiceModel purchaseServiceModel) {
		purchaseRepository.saveAndFlush(modelMapper.map(purchaseServiceModel, Purchase.class));
	}

	@Override
	public List<PurchaseServiceModel> findAll() {
		return purchaseRepository.findAll().stream()
				.map(purchase -> modelMapper.map(purchase, PurchaseServiceModel.class)).collect(toList());
	}

	@Override
	public List<PurchaseServiceModel> findAllByUsername(String username) {
		return purchaseRepository.findAllByUser_Username(username).stream()
				.map(purchase -> modelMapper.map(purchase, PurchaseServiceModel.class)).collect(toList());
	}

	@Override
	public void deleteById(String id) {
		purchaseRepository.deleteById(id);
	}
}
