package dev.popov.bookify.service.event;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.popov.bookify.domain.entity.EventOrder;
import dev.popov.bookify.domain.model.service.EventOrderServiceModel;
import dev.popov.bookify.repository.EventOrderRepository;

@Service
public class EventOrderServiceImpl implements EventOrderService {
	private final EventOrderRepository eventOrderRepository;
	private final ModelMapper modelMapper;

	@Autowired
	public EventOrderServiceImpl(EventOrderRepository eventOrderRepository, ModelMapper modelMapper) {
		this.eventOrderRepository = eventOrderRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public EventOrderServiceModel create(EventOrderServiceModel eventOrderServiceModel) {
		return modelMapper.map(
				eventOrderRepository.saveAndFlush(modelMapper.map(eventOrderServiceModel, EventOrder.class)),
				EventOrderServiceModel.class);
	}

}
