package com.leonardoterrao.ordering.system.order.service.domain;

import com.leonardoterrao.ordering.system.order.service.domain.ports.output.message.publisher.payment.OrderCancelledPaymentRequestMessagePublisher;
import com.leonardoterrao.ordering.system.order.service.domain.ports.output.message.publisher.payment.OrderCreationPaymentRequestMessagePublisher;
import com.leonardoterrao.ordering.system.order.service.domain.ports.output.message.publisher.restaurantapproval.OrderPaidRestaurantRequestMessagePublisher;
import com.leonardoterrao.ordering.system.order.service.domain.ports.output.repository.CustomerRepository;
import com.leonardoterrao.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.leonardoterrao.ordering.system.order.service.domain.ports.output.repository.RestaurantRepository;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "com.leonardoterrao.ordering.system")
public class OrderTestConfiguration {

    @Bean
    public OrderCreationPaymentRequestMessagePublisher orderCreationPaymentRequestMessagePublisher() {
        return Mockito.mock(OrderCreationPaymentRequestMessagePublisher.class);
    }

    @Bean
    public OrderCancelledPaymentRequestMessagePublisher orderCancelledPaymentRequestMessagePublisher() {
        return Mockito.mock(OrderCancelledPaymentRequestMessagePublisher.class);
    }

    @Bean
    public OrderPaidRestaurantRequestMessagePublisher orderPaidRestaurantRequestMessagePublisher() {
        return Mockito.mock(OrderPaidRestaurantRequestMessagePublisher.class);
    }

    @Bean
    public OrderRepository orderRepository() {
        return Mockito.mock(OrderRepository.class);
    }

    @Bean
    public CustomerRepository customerRepository() {
        return Mockito.mock(CustomerRepository.class);
    }

    @Bean
    public RestaurantRepository restaurantRepository() {
        return Mockito.mock(RestaurantRepository.class);
    }

    @Bean
    public OrderDomainService orderDomainService() {
        return new OrderDomainServiceImp();
    }
}
