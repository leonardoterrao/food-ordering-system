package com.leonardoterrao.ordering.system.order.service.dataaccess.order.mapper;

import com.leonardoterrao.ordering.system.domain.valueobject.CustomerId;
import com.leonardoterrao.ordering.system.domain.valueobject.OrderId;
import com.leonardoterrao.ordering.system.domain.valueobject.RestaurantId;
import com.leonardoterrao.ordering.system.order.service.dataaccess.order.entity.OrderAddressEntity;
import com.leonardoterrao.ordering.system.order.service.dataaccess.order.entity.OrderEntity;
import com.leonardoterrao.ordering.system.order.service.dataaccess.order.entity.OrderItemEntity;
import com.leonardoterrao.ordering.system.order.service.domain.entity.Order;
import com.leonardoterrao.ordering.system.order.service.domain.entity.OrderItem;
import com.leonardoterrao.ordering.system.order.service.domain.valueobject.StreetAddress;
import com.leonardoterrao.ordering.system.order.service.domain.valueobject.TrackingId;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.leonardoterrao.ordering.system.order.service.domain.entity.Order.FAILURE_MESSAGE_DELIMITER;

@Component
public class OrderDataAccessMapper {

    public OrderEntity toOrderEntity(Order order) {
        if (order == null) {
            return null;
        }
        OrderEntity orderEntity = OrderEntity.builder()
                .id(order.getId().getValue())
                .customerId(order.getCustomerId().getValue())
                .restaurantId(order.getRestaurantId().getValue())
                .trackingId(order.getTrackingId().getValue())
                .address(deliveryAddressToAddressEntity(order.getDeliveryAddress()))
                .items(orderItemsToOrderItemEntities(order.getItems()))
                .orderStatus(order.getOrderStatus())
                .failureMessages(order.getFailureMessages() != null ?
                        String.join(FAILURE_MESSAGE_DELIMITER, order.getFailureMessages()) : "")
                .build();

        orderEntity.getAddress().setOrder(orderEntity);
        orderEntity.getItems().forEach(oi -> oi.setOrder(orderEntity));
        return orderEntity;
    }

    public Order orderEntityToOrder(OrderEntity orderEntity) {
        if (orderEntity == null) {
            return null;
        }

        return Order.builder()
                .id(new OrderId(orderEntity.getId()))
                .customerId(new CustomerId(orderEntity.getCustomerId()))
                .restaurantId(new RestaurantId(orderEntity.getRestaurantId()))
                .trackingId(new TrackingId(orderEntity.getTrackingId()))
//                .deliveryAddress() TODO continue from here tomorrow!
                .build();
    }

    private List<OrderItemEntity> orderItemsToOrderItemEntities(List<OrderItem> items) {
        return items.stream()
                .map(oi -> OrderItemEntity.builder()
                        .id(oi.getId().getValue())
                        .productId(oi.getProduct().getId().getValue())
                        .order(OrderEntity.builder().id(oi.getOrderId().getValue()).build())
                        .price(oi.getPrice().getAmount())
                        .quantity(oi.getQuantity())
                        .subTotal(oi.getSubTotal().getAmount())
                        .build())
                .toList();
    }

    private OrderAddressEntity deliveryAddressToAddressEntity(StreetAddress deliveryAddress) {
        return OrderAddressEntity.builder()
                .id(deliveryAddress.getId())
                .street(deliveryAddress.getStreet())
                .postalCode(deliveryAddress.getPostalCode())
                .city(deliveryAddress.getCity())
                .build();
    }

}
