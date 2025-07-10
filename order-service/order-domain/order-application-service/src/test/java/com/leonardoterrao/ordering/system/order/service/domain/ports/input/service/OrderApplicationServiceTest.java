package com.leonardoterrao.ordering.system.order.service.domain.ports.input.service;


import com.leonardoterrao.ordering.system.domain.valueobject.CustomerId;
import com.leonardoterrao.ordering.system.domain.valueobject.Money;
import com.leonardoterrao.ordering.system.domain.valueobject.OrderId;
import com.leonardoterrao.ordering.system.domain.valueobject.OrderStatus;
import com.leonardoterrao.ordering.system.domain.valueobject.ProductId;
import com.leonardoterrao.ordering.system.domain.valueobject.RestaurantId;
import com.leonardoterrao.ordering.system.order.service.domain.OrderTestConfiguration;
import com.leonardoterrao.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.leonardoterrao.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.leonardoterrao.ordering.system.order.service.domain.dto.create.OrderAddress;
import com.leonardoterrao.ordering.system.order.service.domain.dto.create.OrderItem;
import com.leonardoterrao.ordering.system.order.service.domain.entity.Customer;
import com.leonardoterrao.ordering.system.order.service.domain.entity.Order;
import com.leonardoterrao.ordering.system.order.service.domain.entity.Product;
import com.leonardoterrao.ordering.system.order.service.domain.entity.Restaurant;
import com.leonardoterrao.ordering.system.order.service.domain.exception.OrderDomainException;
import com.leonardoterrao.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.leonardoterrao.ordering.system.order.service.domain.ports.output.repository.CustomerRepository;
import com.leonardoterrao.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.leonardoterrao.ordering.system.order.service.domain.ports.output.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = OrderTestConfiguration.class)
public class OrderApplicationServiceTest {

    @Autowired
    private OrderApplicationService orderApplicationService;

    @Autowired
    private OrderDataMapper orderDataMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    private CreateOrderCommand createOrderCommand;
    private CreateOrderCommand createOrderCommandWithWrongPrice;
    private CreateOrderCommand createOrderCommandWithWrongProductPrice;
    private final UUID CUSTOMER_ID = UUID.fromString("76abf2ee-c308-40ae-bdd8-522f36f7fe9d");
    private final UUID RESTAURANT_ID = UUID.fromString("4c50038f-2ef4-4356-a419-45bfcee905ff");
    private final UUID ORDER_ID = UUID.fromString("1028f3af-4f87-4da1-bf7d-bde8aa65c5d0");
    private final UUID PRODUCT_ONE_ID = UUID.fromString("342b277a-dfce-49ca-83e8-34d2de68a428");
    private final UUID PRODUCT_TWO_ID = UUID.fromString("5581c27e-431c-4bc6-ba0d-ab6ed3cd4699");
    private final BigDecimal PRICE = BigDecimal.valueOf(200.00);

    @BeforeAll
    public void init() {
        createOrderCommand = CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .address(OrderAddress.builder()
                        .street("123 Main St")
                        .postalCode("12345")
                        .city("Springfield")
                        .build())
                .price(PRICE)
                .items(List.of(
                        OrderItem.builder()
                                .productId(PRODUCT_ONE_ID)
                                .quantity(1)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("50.00"))
                                .build(),
                        OrderItem.builder()
                                .productId(PRODUCT_TWO_ID)
                                .quantity(3)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("150.00"))
                                .build()
                ))
                .build();

        createOrderCommandWithWrongPrice = CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .address(OrderAddress.builder()
                        .street("123 Main St")
                        .postalCode("12345")
                        .city("Springfield")
                        .build())
                .price(new BigDecimal("300.00")) // Wrong price
                .items(List.of(
                        OrderItem.builder()
                                .productId(PRODUCT_ONE_ID)
                                .quantity(1)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("50.00"))
                                .build(),
                        OrderItem.builder()
                                .productId(PRODUCT_TWO_ID)
                                .quantity(3)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("150.00"))
                                .build()
                ))
                .build();

        createOrderCommandWithWrongProductPrice = CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .address(OrderAddress.builder()
                        .street("123 Main St")
                        .postalCode("12345")
                        .city("Springfield")
                        .build())
                .price(new BigDecimal("210.00")) // Wrong price
                .items(List.of(
                        OrderItem.builder()
                                .productId(PRODUCT_ONE_ID)
                                .quantity(1)
                                .price(new BigDecimal("60.00"))
                                .subTotal(new BigDecimal("60.00"))
                                .build(),
                        OrderItem.builder()
                                .productId(PRODUCT_TWO_ID)
                                .quantity(3)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("150.00"))
                                .build()
                ))
                .build();

        final Customer customer = new Customer();
        customer.setId(new CustomerId(CUSTOMER_ID));

        final Restaurant restaurant = Restaurant.builder()
                .active(true)
                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                .products(List.of(
                        new Product(new ProductId(PRODUCT_ONE_ID), "Pizza One", new Money(new BigDecimal("50.00"))),
                        new Product(new ProductId(PRODUCT_TWO_ID), "Pizza Two", new Money(new BigDecimal("50.00")))
                ))
                .build();

        final Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        order.setId(new OrderId(ORDER_ID));

        when(customerRepository.findCustomer(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        when(restaurantRepository.findRestaurantInformation(orderDataMapper.createOrderCommandToRestaurant(createOrderCommand))).thenReturn(Optional.of(restaurant));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
    }

    @Test
    public void testCreateOrder() {
        final CreateOrderResponse orderResponse = orderApplicationService.createOrder(createOrderCommand);

        assertThat(orderResponse).isNotNull();
        assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(orderResponse.getMessage()).isEqualTo("Order created successfully");
        assertThat(orderResponse.getOrderTrackingId()).isNotNull();
    }

    @Test
    public void testCreateOrderWithWrongTotalPrice() {
        final OrderDomainException orderDomainException = assertThrows(
                OrderDomainException.class,
                () -> orderApplicationService.createOrder(createOrderCommandWithWrongPrice));
        assertThat(orderDomainException.getMessage())
                .isEqualTo("Order items total must be equal to order price");
    }

    @Test
    public void testCreateOrderWithWrongProductPrice() {
        final OrderDomainException orderDomainException = assertThrows(
                OrderDomainException.class,
                () -> orderApplicationService.createOrder(createOrderCommandWithWrongProductPrice));
        assertThat(orderDomainException.getMessage())
                .isEqualTo("Order item price 60.00 is not valid for product: 342b277a-dfce-49ca-83e8-34d2de68a428");
    }

    @Test
    public void testCreateOrderWithPassiveRestaurant() {
        final Restaurant restaurantResponse = Restaurant.builder()
                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                .products(List.of(new Product(new ProductId(PRODUCT_ONE_ID), "product-1", new Money(new BigDecimal("50.00"))),
                        new Product(new ProductId(PRODUCT_TWO_ID), "product-2", new Money(new BigDecimal("50.00")))))
                .active(false)
                .build();
        when(restaurantRepository.findRestaurantInformation(orderDataMapper.createOrderCommandToRestaurant(createOrderCommand)))
                .thenReturn(Optional.of(restaurantResponse));
        final OrderDomainException orderDomainException = assertThrows(OrderDomainException.class,
                () -> orderApplicationService.createOrder(createOrderCommand));
        assertThat(orderDomainException.getMessage()).isEqualTo("Restaurant with id: 4c50038f-2ef4-4356-a419-45bfcee905ff is not active");
    }
}
