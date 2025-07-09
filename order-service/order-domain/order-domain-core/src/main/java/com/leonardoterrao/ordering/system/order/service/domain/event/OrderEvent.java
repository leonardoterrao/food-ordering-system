package com.leonardoterrao.ordering.system.order.service.domain.event;

import com.leonardoterrao.ordering.system.domain.event.DomainEvent;
import com.leonardoterrao.ordering.system.order.service.domain.entity.Order;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.ZonedDateTime;

@Getter
@RequiredArgsConstructor
public abstract class OrderEvent implements DomainEvent<Order> {

    private final Order order;
    private final ZonedDateTime createdAt;

}
