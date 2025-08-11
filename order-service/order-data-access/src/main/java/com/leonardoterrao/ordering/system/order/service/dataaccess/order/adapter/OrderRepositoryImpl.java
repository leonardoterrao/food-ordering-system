package com.leonardoterrao.ordering.system.order.service.dataaccess.order.adapter;

import com.leonardoterrao.ordering.system.order.service.dataaccess.order.entity.OrderEntity;
import com.leonardoterrao.ordering.system.order.service.dataaccess.order.mapper.OrderDataAccessMapper;
import com.leonardoterrao.ordering.system.order.service.dataaccess.order.repository.OrderJpaRepository;
import com.leonardoterrao.ordering.system.order.service.domain.entity.Order;
import com.leonardoterrao.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.leonardoterrao.ordering.system.order.service.domain.valueobject.TrackingId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderDataAccessMapper orderDataAccessMapper;

    @Override
    public Order save(final Order order) {
        final OrderEntity orderEntity = orderDataAccessMapper.toOrderEntity(order);
        return orderDataAccessMapper.orderEntityToOrder(orderJpaRepository.save(orderEntity));
    }

    @Override
    public Optional<Order> findByTrackingId(final TrackingId trackingId) {
        return orderJpaRepository.findByTrackingId(trackingId.getValue())
                .map(orderDataAccessMapper::orderEntityToOrder);
    }

}
