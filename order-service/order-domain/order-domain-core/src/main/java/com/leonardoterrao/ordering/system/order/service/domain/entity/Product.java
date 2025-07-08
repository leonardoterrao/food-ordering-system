package com.leonardoterrao.ordering.system.order.service.domain.entity;

import com.leonardoterrao.ordering.system.domain.entity.BaseEntity;
import com.leonardoterrao.ordering.system.domain.valueobject.Money;
import com.leonardoterrao.ordering.system.domain.valueobject.ProductId;
import lombok.Getter;

@Getter
public class Product extends BaseEntity<ProductId> {

    private final String name;
    private final Money price;

    public Product(final ProductId productId, final String name, final Money price) {
        super();
        super.setId(productId);
        this.name = name;
        this.price = price;
    }

}
