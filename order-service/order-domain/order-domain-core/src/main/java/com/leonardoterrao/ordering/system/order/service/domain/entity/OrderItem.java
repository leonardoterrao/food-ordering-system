package com.leonardoterrao.ordering.system.order.service.domain.entity;

import com.leonardoterrao.ordering.system.domain.entity.BaseEntity;
import com.leonardoterrao.ordering.system.domain.valueobject.Money;
import com.leonardoterrao.ordering.system.domain.valueobject.OrderId;
import com.leonardoterrao.ordering.system.order.service.domain.valueobject.OrderItemId;
import lombok.Getter;

@Getter
public class OrderItem extends BaseEntity<OrderItemId> {

    private OrderId orderId;
    private final Product product;
    private final int quantity;
    private final Money price;
    private final Money subTotal;

    public void initializeOrderItem(final OrderId orderId, final OrderItemId orderItemId) {
        super.setId(orderItemId);
        this.orderId = orderId;
    }

    public boolean isPriceValid() {
        return price.isGreaterThanZero()
                && price.equals(product.getPrice())
                && price.multiply(quantity).equals(subTotal);
    }

    private OrderItem(final Builder builder) {
        super.setId(builder.orderItemId);
        this.product = builder.product;
        this.quantity = builder.quantity;
        this.price = builder.price;
        this.subTotal = builder.subTotal;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private OrderItemId orderItemId;
        private Product product;
        private int quantity;
        private Money price;
        private Money subTotal;

        private Builder() {
        }

        public Builder id(final OrderItemId val) {
            this.orderItemId = val;
            return this;
        }

        public Builder product(final Product val) {
            this.product = val;
            return this;
        }

        public Builder quantity(final int val) {
            this.quantity = val;
            return this;
        }

        public Builder price(final Money val) {
            this.price = val;
            return this;
        }

        public Builder subTotal(final Money val) {
            this.subTotal = val;
            return this;
        }

        public OrderItem build() {
            return new OrderItem(this);
        }
    }
}
