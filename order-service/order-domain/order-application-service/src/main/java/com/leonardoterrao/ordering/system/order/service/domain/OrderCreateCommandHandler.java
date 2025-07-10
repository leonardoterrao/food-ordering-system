package com.leonardoterrao.ordering.system.order.service.domain;


import com.leonardoterrao.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.leonardoterrao.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.leonardoterrao.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.leonardoterrao.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.leonardoterrao.ordering.system.order.service.domain.ports.output.message.publisher.payment.OrderCreationPaymentRequestMessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class OrderCreateCommandHandler {

    private final OrderCreateHelper orderCreateHelper;
    private final OrderDataMapper orderDataMapper;
    private final OrderCreationPaymentRequestMessagePublisher orderCreationPaymentRequestMessagePublisher;

    public CreateOrderResponse createOrder(final CreateOrderCommand createOrderCommand) {
        final OrderCreatedEvent orderCreatedEvent = orderCreateHelper.persistOrder(createOrderCommand);
        log.info("Order with id: {} is created", orderCreatedEvent.getOrder().getId().getValue());
        orderCreationPaymentRequestMessagePublisher.publish(orderCreatedEvent);
        return orderDataMapper.orderToCreateOrderResponse(orderCreatedEvent.getOrder(), "Order created successfully");
    }

}
