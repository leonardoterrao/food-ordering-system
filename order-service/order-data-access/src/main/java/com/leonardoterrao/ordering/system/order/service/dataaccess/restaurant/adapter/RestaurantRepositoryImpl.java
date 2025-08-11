package com.leonardoterrao.ordering.system.order.service.dataaccess.restaurant.adapter;

import com.leonardoterrao.ordering.system.order.service.dataaccess.restaurant.entity.RestaurantEntity;
import com.leonardoterrao.ordering.system.order.service.dataaccess.restaurant.mapper.RestaurantDataAccessMapper;
import com.leonardoterrao.ordering.system.order.service.dataaccess.restaurant.repository.RestaurantJpaRepository;
import com.leonardoterrao.ordering.system.order.service.domain.entity.Restaurant;
import com.leonardoterrao.ordering.system.order.service.domain.ports.output.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RestaurantRepositoryImpl implements RestaurantRepository {

    private final RestaurantJpaRepository restaurantJpaRepository;
    private final RestaurantDataAccessMapper restaurantDataAccessMapper;

    @Override
    public Optional<Restaurant> findRestaurantInformation(final Restaurant restaurant) {
        final List<UUID> restaurantProducts = restaurantDataAccessMapper.restaurantToRestaurantProducts(restaurant);
        final Optional<List<RestaurantEntity>> entities = restaurantJpaRepository.findByRestaurantIdAndProductIdIn(restaurant.getId().getValue(), restaurantProducts);
        return entities.map(restaurantDataAccessMapper::restaurantEntityToRestaurant);
    }
}
