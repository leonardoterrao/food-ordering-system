package com.leonardoterrao.ordering.system.order.service.domain;


import com.leonardoterrao.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.leonardoterrao.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.leonardoterrao.ordering.system.order.service.domain.entity.Customer;
import com.leonardoterrao.ordering.system.order.service.domain.entity.Order;
import com.leonardoterrao.ordering.system.order.service.domain.entity.Restaurant;
import com.leonardoterrao.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.leonardoterrao.ordering.system.order.service.domain.exception.OrderDomainException;
import com.leonardoterrao.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.leonardoterrao.ordering.system.order.service.domain.ports.output.repository.CustomerRepository;
import com.leonardoterrao.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.leonardoterrao.ordering.system.order.service.domain.ports.output.repository.RestaurantRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Component
public class OrderCreateCommandHandler {

    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderDataMapper orderDataMapper;

    @Transactional
    public CreateOrderResponse createOrder(final CreateOrderCommand createOrderCommand) {
        checkCustomer(createOrderCommand.getCustomerId());
        final Restaurant restaurant = checkRestaurant(createOrderCommand);
        final Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        final OrderCreatedEvent orderCreatedEvent = orderDomainService.validateAndInitiateOrder(order, restaurant);
        final Order savedOrder = saveOrder(order);
        log.info("Order with id: {} is created", savedOrder.getId().getValue());
        return orderDataMapper.orderToCreateOrderResponse(savedOrder);
    }

    private Restaurant checkRestaurant(final CreateOrderCommand createOrderCommand) {
        final Restaurant restaurant = orderDataMapper.createOrderCommandToRestaurant(createOrderCommand);
        final Optional<Restaurant> optionalRestaurant = restaurantRepository.findRestaurantInformation(restaurant);
        if (optionalRestaurant.isEmpty()) {
            log.warn("Could not find restaurant with id: {}", createOrderCommand.getRestaurantId());
            throw new OrderDomainException("Could not find restaurant with id: " + createOrderCommand.getRestaurantId());
        }
        return optionalRestaurant.get();
    }

    private void checkCustomer(@NotNull final UUID customerId) {
        final Optional<Customer> customer = customerRepository.findCustomer(customerId);
        if (customer.isEmpty()) {
            log.warn("Could not find customer with id: {}", customerId);
            throw new OrderDomainException("\"Could not find customer with id: " + customerId.toString());
        }
    }

    private Order saveOrder(final Order order) {
        final Order savedOrder = orderRepository.save(order);
        if (savedOrder == null) {
            log.error("Could not save order with id: {}", order.getId().getValue());
            throw new OrderDomainException("Could not save order with id: " + order.getId().getValue());
        }
        log.info("Order with id: {} is saved", savedOrder.getId().getValue());
        return savedOrder;
    }

}
