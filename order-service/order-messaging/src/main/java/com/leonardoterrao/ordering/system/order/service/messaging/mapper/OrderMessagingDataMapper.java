package com.leonardoterrao.ordering.system.order.service.messaging.mapper;


import com.leonardoterrao.ordering.system.kafka.order.avro.model.PaymentOrderStatus;
import com.leonardoterrao.ordering.system.kafka.order.avro.model.PaymentRequestAvroModel;
import com.leonardoterrao.ordering.system.kafka.order.avro.model.Product;
import com.leonardoterrao.ordering.system.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.leonardoterrao.ordering.system.kafka.order.avro.model.RestaurantOrderStatus;
import com.leonardoterrao.ordering.system.order.service.domain.entity.Order;
import com.leonardoterrao.ordering.system.order.service.domain.event.OrderCancelledEvent;
import com.leonardoterrao.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.leonardoterrao.ordering.system.order.service.domain.event.OrderPaidEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderMessagingDataMapper {

    public PaymentRequestAvroModel orderCreateEventToPaymentRequestAvroModel(final OrderCreatedEvent orderCreatedEvent) {
        final Order order = orderCreatedEvent.getOrder();
        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID())
                .setSagaId(UUID.randomUUID())
                .setCustomerId(order.getCustomerId().getValue())
                .setOrderId(order.getId().getValue())
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(orderCreatedEvent.getCreatedAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.PENDING)
                .build();
    }

    public PaymentRequestAvroModel orderCanceledEventToPaymentRequestAvroModel(final OrderCancelledEvent orderCancelledEvent) {
        final Order order = orderCancelledEvent.getOrder();
        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID())
                .setSagaId(UUID.randomUUID())
                .setCustomerId(order.getCustomerId().getValue())
                .setOrderId(order.getId().getValue())
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(orderCancelledEvent.getCreatedAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.CANCELLED)
                .build();
    }

    public RestaurantApprovalRequestAvroModel orderPaidEventToRestaurantApprovalRequestAvroModel(final OrderPaidEvent orderPaidEvent) {
        final Order order = orderPaidEvent.getOrder();
        return RestaurantApprovalRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID())
                .setSagaId(UUID.randomUUID())
                .setOrderId(order.getId().getValue())
                .setRestaurantId(order.getRestaurantId().getValue())
                .setRestaurantOrderStatus(RestaurantOrderStatus.valueOf(order.getOrderStatus().name()))
                .setProducts(order.getItems().stream().map(oi -> Product.newBuilder()
                        .setId(oi.getProduct().getId().getValue().toString())
                        .setQuantity(oi.getQuantity())
                        .build())
                        .toList())
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(orderPaidEvent.getCreatedAt().toInstant())
                .setRestaurantOrderStatus(RestaurantOrderStatus.PAID)
                .build();
    }

}
