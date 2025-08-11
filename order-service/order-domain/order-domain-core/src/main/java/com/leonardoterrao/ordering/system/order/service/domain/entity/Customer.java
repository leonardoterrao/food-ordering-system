package com.leonardoterrao.ordering.system.order.service.domain.entity;

import com.leonardoterrao.ordering.system.domain.entity.AggregateRoot;
import com.leonardoterrao.ordering.system.domain.valueobject.CustomerId;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Customer extends AggregateRoot<CustomerId> {

    public Customer(final CustomerId customerId) {
        super.setId(customerId);
    }

}
