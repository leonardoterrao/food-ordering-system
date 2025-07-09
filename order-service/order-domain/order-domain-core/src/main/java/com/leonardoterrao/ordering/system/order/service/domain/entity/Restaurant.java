package com.leonardoterrao.ordering.system.order.service.domain.entity;

import com.leonardoterrao.ordering.system.domain.entity.AggregateRoot;
import com.leonardoterrao.ordering.system.domain.valueobject.RestaurantId;
import lombok.Getter;

import java.util.List;

@Getter
public class Restaurant extends AggregateRoot<RestaurantId> {

    private final List<Product> products;
    private final boolean active;

    private Restaurant(final Builder builder) {
        super.setId(builder.restaurantId);
        products = builder.products;
        active = builder.active;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private RestaurantId restaurantId;
        private List<Product> products;
        private boolean active;

        private Builder() {
        }

        public Builder restaurantId(final RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public Builder products(final List<Product> val) {
            products = val;
            return this;
        }

        public Builder active(final boolean val) {
            active = val;
            return this;
        }

        public Restaurant build() {
            return new Restaurant(this);
        }
    }
}
