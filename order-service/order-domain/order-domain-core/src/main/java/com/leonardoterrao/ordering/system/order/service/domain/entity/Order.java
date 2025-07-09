package com.leonardoterrao.ordering.system.order.service.domain.entity;

import com.leonardoterrao.ordering.system.domain.entity.AggregateRoot;
import com.leonardoterrao.ordering.system.domain.valueobject.CustomerId;
import com.leonardoterrao.ordering.system.domain.valueobject.Money;
import com.leonardoterrao.ordering.system.domain.valueobject.OrderId;
import com.leonardoterrao.ordering.system.domain.valueobject.OrderStatus;
import com.leonardoterrao.ordering.system.domain.valueobject.RestaurantId;
import com.leonardoterrao.ordering.system.order.service.domain.exception.OrderDomainException;
import com.leonardoterrao.ordering.system.order.service.domain.valueobject.OrderItemId;
import com.leonardoterrao.ordering.system.order.service.domain.valueobject.StreetAddress;
import com.leonardoterrao.ordering.system.order.service.domain.valueobject.TrackingId;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class Order extends AggregateRoot<OrderId> {

    private final CustomerId customerId;
    private final RestaurantId restaurantId;
    private final StreetAddress streetAddress;
    private final Money price;
    private final List<OrderItem> items;

    private TrackingId trackingId;
    private OrderStatus orderStatus;
    private List<String> failureMessages;

    public void initializeOrder() {
        setId(new OrderId(UUID.randomUUID()));
        trackingId = new TrackingId(UUID.randomUUID());
        orderStatus = OrderStatus.PENDING;
        initializeOrderItems();
    }

    public void validateOrder() {
        validateInitialOrder();
        validateTotalPrice();
        validateItemsPrice();
    }

    public void pay() {
        if (orderStatus != OrderStatus.PENDING) {
            throw new OrderDomainException("Order is not in correct state for pay operation");
        }
        orderStatus = OrderStatus.PAID;
    }

    public void approve() {
        if (orderStatus != OrderStatus.PAID) {
            throw new OrderDomainException("Order is not in correct state for approve operation");
        }
        orderStatus = OrderStatus.APPROVED;
    }

    public void initCancel(final List<String> failureMessages) {
        if (orderStatus != OrderStatus.PAID) {
            throw new OrderDomainException("Order is not in correct state for cancel operation");
        }
        orderStatus = OrderStatus.CANCELLING;
        updateFailureMessages(failureMessages);
    }

    public void cancel(final List<String> failureMessages) {
        if (!(orderStatus == OrderStatus.CANCELLING || orderStatus == OrderStatus.PENDING)) {
            throw new OrderDomainException("Order is not in correct state for cancel operation");
        }
        orderStatus = OrderStatus.CANCELLED;
        updateFailureMessages(failureMessages);
    }

    private void updateFailureMessages(final List<String> failureMessages) {
        final List<String> validMessages = failureMessages.stream().filter(message -> !message.isEmpty()).toList();
        if (this.failureMessages != null) {
            this.failureMessages.addAll(validMessages);
        } else {
            this.failureMessages = validMessages;
        }
    }

    private void validateItemsPrice() {
        final Money orderItemsTotal = items.stream().map(i -> {
            validateItemPrice(i);
            return i.getSubTotal();
        }).reduce(Money.ZERO, Money::add);

        if (!orderItemsTotal.equals(price)) {
            throw new OrderDomainException("Order items total must be equal to order price");
        }
    }

    private static void validateItemPrice(final OrderItem orderItem) {
        if (!orderItem.isPriceValid()) {
            throw new OrderDomainException("Order item price "
                    + orderItem.getPrice().getAmount() + " is not valid: " + orderItem.getId().getValue());
        }
    }

    private void validateTotalPrice() {
        if (price == null || !price.isGreaterThanZero()) {
            throw new OrderDomainException("Order price must be greater than zero");
        }
    }

    private void validateInitialOrder() {
        if (orderStatus != null || getId() != null) {
            throw new OrderDomainException("Order is not in correct state for initialization");
        }
    }


    private void initializeOrderItems() {
        long itemId = 1;
        for (final OrderItem orderItem : items) {
            orderItem.initializeOrderItem(super.getId(), new OrderItemId(itemId++));
        }
    }

    private Order(final Builder builder) {
        super.setId(builder.orderId);
        this.customerId = builder.customerId;
        this.restaurantId = builder.restaurantId;
        this.streetAddress = builder.streetAddress;
        this.price = builder.price;
        this.items = builder.orderItems;
        this.trackingId = builder.trackingId;
        this.orderStatus = builder.orderStatus;
        this.failureMessages = builder.failureMessages;
    }

    public static final class Builder {
        private OrderId orderId;
        private CustomerId customerId;
        private RestaurantId restaurantId;
        private StreetAddress streetAddress;
        private Money price;
        private List<OrderItem> orderItems;
        private TrackingId trackingId;
        private OrderStatus orderStatus;
        private List<String> failureMessages;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(final OrderId val) {
            this.orderId = val;
            return this;
        }

        public Builder customerId(final CustomerId val) {
            this.customerId = val;
            return this;
        }

        public Builder restaurantId(final RestaurantId val) {
            this.restaurantId = val;
            return this;
        }

        public Builder streetAddress(final StreetAddress val) {
            this.streetAddress = val;
            return this;
        }

        public Builder price(final Money val) {
            this.price = val;
            return this;
        }

        public Builder orderItems(final List<OrderItem> val) {
            this.orderItems = val;
            return this;
        }

        public Builder trackingId(final TrackingId val) {
            this.trackingId = val;
            return this;
        }

        public Builder orderStatus(final OrderStatus val) {
            this.orderStatus = val;
            return this;
        }

        public Builder failureMessages(final List<String> val) {
            this.failureMessages = val;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
