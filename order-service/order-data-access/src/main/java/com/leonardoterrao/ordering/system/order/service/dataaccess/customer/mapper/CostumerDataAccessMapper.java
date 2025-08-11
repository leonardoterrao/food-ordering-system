package com.leonardoterrao.ordering.system.order.service.dataaccess.customer.mapper;

import com.leonardoterrao.ordering.system.domain.valueobject.CustomerId;
import com.leonardoterrao.ordering.system.order.service.dataaccess.customer.entity.CustomerEntity;
import com.leonardoterrao.ordering.system.order.service.domain.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CostumerDataAccessMapper {

    public Customer customerToCustomerEntity(final CustomerEntity customerEntity) {
        return new Customer(new CustomerId(customerEntity.getId()));
    }


}
