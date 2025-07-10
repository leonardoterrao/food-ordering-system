package com.leonardoterrao.ordering.system.order.service.domain.mapper;

import com.leonardoterrao.ordering.system.domain.valueobject.CustomerId;
import com.leonardoterrao.ordering.system.domain.valueobject.Money;
import com.leonardoterrao.ordering.system.domain.valueobject.ProductId;
import com.leonardoterrao.ordering.system.domain.valueobject.RestaurantId;
import com.leonardoterrao.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.leonardoterrao.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.leonardoterrao.ordering.system.order.service.domain.dto.create.OrderAddress;
import com.leonardoterrao.ordering.system.order.service.domain.dto.track.TrackOrderResponse;
import com.leonardoterrao.ordering.system.order.service.domain.entity.Order;
import com.leonardoterrao.ordering.system.order.service.domain.entity.OrderItem;
import com.leonardoterrao.ordering.system.order.service.domain.entity.Product;
import com.leonardoterrao.ordering.system.order.service.domain.entity.Restaurant;
import com.leonardoterrao.ordering.system.order.service.domain.valueobject.StreetAddress;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderDataMapper {

    public Restaurant createOrderCommandToRestaurant(final CreateOrderCommand createOrderCommand) {
        return Restaurant.builder()
                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                .products(createOrderCommand.getItems().stream()
                        .map(item -> new Product(new ProductId(item.getProductId())))
                        .toList())
                .build();
    }

    public Order createOrderCommandToOrder(final CreateOrderCommand createOrderCommand) {
        return Order.builder()
                .customerId(new CustomerId(createOrderCommand.getCustomerId()))
                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                .deliveryAddress(orderAddressToStreetAddress(createOrderCommand.getAddress()))
                .price(new Money(createOrderCommand.getPrice()))
                .orderItems(orderItemsToOrderItemsEntity(createOrderCommand.getItems()))
                .build();
    }

    public CreateOrderResponse orderToCreateOrderResponse(@NotNull final Order order) {
        return CreateOrderResponse.builder()
                .orderTrackingId(order.getTrackingId().getValue())
                .orderStatus(order.getOrderStatus())
                .build();
    }

    public TrackOrderResponse orderToTrackOrderResponse(final Order order) {
        return TrackOrderResponse.builder()
                .orderTrackingId(order.getTrackingId().getValue())
                .orderStatus(order.getOrderStatus())
                .failureMessages(order.getFailureMessages())
                .build();

    }

    private List<OrderItem> orderItemsToOrderItemsEntity(@NotNull final List<com.leonardoterrao.ordering.system.order.service.domain.dto.create.OrderItem> items) {
        return items.stream()
                .map(item -> OrderItem.builder()
                        .product(new Product(new ProductId(item.getProductId())))
                        .quantity(item.getQuantity())
                        .price(new Money(item.getPrice()))
                        .subTotal(new Money(item.getSubTotal()))
                        .build()).collect(Collectors.toList());
    }

    private StreetAddress orderAddressToStreetAddress(@NotNull final OrderAddress address) {
        return new StreetAddress(UUID.randomUUID(),
                address.getStreet(),
                address.getPostalCode(),
                address.getCity());
    }

}
