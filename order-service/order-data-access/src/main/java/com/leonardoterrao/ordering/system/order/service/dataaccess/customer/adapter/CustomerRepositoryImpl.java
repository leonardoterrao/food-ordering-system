package com.leonardoterrao.ordering.system.order.service.dataaccess.customer.adapter;

import com.leonardoterrao.ordering.system.order.service.dataaccess.customer.mapper.CostumerDataAccessMapper;
import com.leonardoterrao.ordering.system.order.service.dataaccess.customer.repository.CustomerJpaRepository;
import com.leonardoterrao.ordering.system.order.service.domain.entity.Customer;
import com.leonardoterrao.ordering.system.order.service.domain.ports.output.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerJpaRepository customerJpaRepository;
    private final CostumerDataAccessMapper costumerDataAccessMapper;

    @Override
    public Optional<Customer> findCustomer(final UUID customerId) {
        return customerJpaRepository.findById(customerId)
                .map(costumerDataAccessMapper::customerToCustomerEntity);
    }

}
