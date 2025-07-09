package com.leonardoterrao.ordering.system.order.service.domain;

import com.leonardoterrao.ordering.system.domain.valueobject.ProductId;
import com.leonardoterrao.ordering.system.order.service.domain.entity.Order;
import com.leonardoterrao.ordering.system.order.service.domain.entity.Product;
import com.leonardoterrao.ordering.system.order.service.domain.entity.Restaurant;
import com.leonardoterrao.ordering.system.order.service.domain.event.OrderCancelledEvent;
import com.leonardoterrao.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.leonardoterrao.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.leonardoterrao.ordering.system.order.service.domain.exception.OrderDomainException;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class OrderDomainServiceImp implements OrderDomainService {

    @Override
    public OrderCreatedEvent validateAndInitiateOrder(final Order order, final Restaurant restaurant) {
        validateRestaurant(restaurant);
        setOrderProductInformation(order, restaurant);
        order.validateOrder();
        order.initializeOrder();
        log.info("Order with id: {} is successfully initialized", order.getId().getValue());
        return new OrderCreatedEvent(order, ZonedDateTime.now(ZoneOffset.UTC));
    }

    @Override
    public OrderPaidEvent payOrder(final Order order) {
        order.pay();
        log.info("Order with id: {} is paid", order.getId().getValue());
        return new OrderPaidEvent(order, ZonedDateTime.now(ZoneOffset.UTC));
    }

    @Override
    public void approveOrder(final Order order) {
        order.approve();
        log.info("Order with id: {} is approved", order.getId().getValue());
    }

    @Override
    public OrderCancelledEvent cancelOrderPayment(final Order order, final List<String> errorMessages) {
        order.initCancel(errorMessages);
        log.info("Order payment is cancelling for order id: {}", order.getId().getValue());
        return new OrderCancelledEvent(order, ZonedDateTime.now(ZoneOffset.UTC));
    }

    @Override
    public void cancelOrder(final Order order, final List<String> errorMessages) {
        order.cancel(errorMessages);
        log.info("Order with id: {} is cancelled", order.getId().getValue());
    }

    private static void validateRestaurant(final Restaurant restaurant) {
        if (!restaurant.isActive()) {
            throw new OrderDomainException(
                    String.format("Restaurant with id: %s is not active", restaurant.getId().getValue())
            );
        }
    }

    private static void setOrderProductInformation(final Order order, final Restaurant restaurant) {
        final Map<ProductId, Product> products = restaurant.getProducts().stream().collect(
                Collectors.toMap(Product::getId, product -> product)
        );

        order.getItems().forEach(orderItem -> {
            final var currentProduct = orderItem.getProduct();
            final var restaurantProduct = products.get(currentProduct.getId());
            if (currentProduct.equals(restaurantProduct)) {
                currentProduct.updateWithConfirmedNameAndPrice(restaurantProduct.getName(), restaurantProduct.getPrice());
            }
        });
    }


}
