package com.leonardoterrao.ordering.system.order.service.domain.ports.output.message.publisher.payment;

import com.leonardoterrao.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.leonardoterrao.ordering.system.order.service.domain.event.OrderCancelledEvent;

public interface OrderCancelledPaymentRequestMessagePublisher extends DomainEventPublisher<OrderCancelledEvent> {
}
