package com.leonardoterrao.ordering.system.order.service.domain;

import com.leonardoterrao.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.leonardoterrao.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.leonardoterrao.ordering.system.order.service.domain.dto.track.TrackOrderQuery;
import com.leonardoterrao.ordering.system.order.service.domain.dto.track.TrackOrderResponse;
import com.leonardoterrao.ordering.system.order.service.domain.ports.input.service.OrderApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@RequiredArgsConstructor
@Slf4j
@Validated
@Service
class OrderApplicationServiceImpl implements OrderApplicationService {

    private final OrderCreateCommandHandler orderCreateCommandHandler;
    private final OrderTrackCommandHandler orderTrackCommandHandler;

    @Override
    public CreateOrderResponse createOrder(final CreateOrderCommand createOrderCommand) {
        return orderCreateCommandHandler.createOrder(createOrderCommand);
    }

    @Override
    public TrackOrderResponse trakcOrder(final TrackOrderQuery trackOrderResponse) {
        return orderTrackCommandHandler.trackOrder(trackOrderResponse);
    }

}
