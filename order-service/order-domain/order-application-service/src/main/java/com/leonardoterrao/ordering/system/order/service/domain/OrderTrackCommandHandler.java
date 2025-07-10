package com.leonardoterrao.ordering.system.order.service.domain;


import com.leonardoterrao.ordering.system.order.service.domain.dto.track.TrackOrderQuery;
import com.leonardoterrao.ordering.system.order.service.domain.dto.track.TrackOrderResponse;
import com.leonardoterrao.ordering.system.order.service.domain.entity.Order;
import com.leonardoterrao.ordering.system.order.service.domain.exception.OrderNotFoundException;
import com.leonardoterrao.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.leonardoterrao.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.leonardoterrao.ordering.system.order.service.domain.valueobject.TrackingId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Component
public class OrderTrackCommandHandler {

    private final OrderDataMapper orderDataMapper;
    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public TrackOrderResponse trackOrder(final TrackOrderQuery trackOrderQuery) {
        final Optional<Order> order = orderRepository.findByTrackingId(new TrackingId(trackOrderQuery.getOrderTrackingId()));
        if (order.isEmpty()) {
            log.warn("Could not find order with tracking id: {}", trackOrderQuery.getOrderTrackingId());
            throw new OrderNotFoundException(String.format("Could not find order with tracking id: %s",
                    trackOrderQuery.getOrderTrackingId()));
        }

        return orderDataMapper.orderToTrackOrderResponse(order.get());
    }

}
