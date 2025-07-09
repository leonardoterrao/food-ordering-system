package com.leonardoterrao.ordering.system.order.service.domain.entity;

import com.leonardoterrao.ordering.system.domain.entity.BaseEntity;
import com.leonardoterrao.ordering.system.domain.valueobject.Money;
import com.leonardoterrao.ordering.system.domain.valueobject.ProductId;
import lombok.Getter;

@Getter
public class Product extends BaseEntity<ProductId> {

    private String name;
    private Money price;

    public Product(final ProductId productId) {
        super.setId(productId);
    }

    public Product(final ProductId productId, final String name, final Money price) {
        super.setId(productId);
        this.name = name;
        this.price = price;
    }

    public void updateWithConfirmedNameAndPrice(final String name, final Money price) {
        this.name = name;
        this.price = price;
    }

}
