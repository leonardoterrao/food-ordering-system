package com.leonardoterrao.ordering.system.order.service.dataaccess.restaurant.mapper;

import com.leonardoterrao.ordering.system.domain.valueobject.Money;
import com.leonardoterrao.ordering.system.domain.valueobject.ProductId;
import com.leonardoterrao.ordering.system.domain.valueobject.RestaurantId;
import com.leonardoterrao.ordering.system.order.service.dataaccess.restaurant.entity.RestaurantEntity;
import com.leonardoterrao.ordering.system.order.service.dataaccess.restaurant.exception.RestaurantDataAccessException;
import com.leonardoterrao.ordering.system.order.service.domain.entity.Product;
import com.leonardoterrao.ordering.system.order.service.domain.entity.Restaurant;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class RestaurantDataAccessMapper {

    public List<UUID> restaurantToRestaurantProducts(final Restaurant restaurant) {
        return restaurant
                .getProducts().stream()
                .map(p -> p.getId().getValue())
                .toList();
    }

    public Restaurant restaurantEntityToRestaurant(final List<RestaurantEntity> restaurantEntities) {
        final RestaurantEntity restaurantEntity = restaurantEntities.stream().findFirst()
                .orElseThrow(() -> new RestaurantDataAccessException("Restaurant not found"));

        final List<Product> products = restaurantEntities.stream()
                .map(e -> new Product(
                        new ProductId(e.getProductId()),
                        e.getProductName(),
                        new Money(e.getProductPrice())))
                .toList();

        return Restaurant.builder()
                .restaurantId(new RestaurantId(restaurantEntity.getRestaurantId()))
                .products(products)
                .active(restaurantEntity.getRestaurantActive())
                .build();
    }

}
