package com.leonardoterrao.ordering.system.order.service.domain.entity;

import com.leonardoterrao.ordering.system.domain.entity.AggregateRoot;
import com.leonardoterrao.ordering.system.domain.valueobject.*;
import com.leonardoterrao.ordering.system.order.service.domain.valueobject.StreetAddress;
import com.leonardoterrao.ordering.system.order.service.domain.valueobject.TrackingId;
import lombok.Getter;

import java.util.List;

@Getter
public class Order extends AggregateRoot<OrderId> {

    private final CustomerId customerId;
    private final RestaurantId restaurantId;
    private final StreetAddress streetAddress;
    private final Money price;
    private final List<OrderItem> orderItems;

    private final TrackingId trackingId;
    private final OrderStatus orderStatus;
    private final List<String> failureMessages;

    private Order(final Builder builder) {
        super.setId(builder.orderId);
        this.customerId = builder.customerId;
        this.restaurantId = builder.restaurantId;
        this.streetAddress = builder.streetAddress;
        this.price = builder.price;
        this.orderItems = builder.orderItems;
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
