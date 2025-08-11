package com.leonardoterrao.ordering.system.order.service.dataaccess.restaurant.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"restaurantId", "productId"})
public class RestaurantEntityId {

    private UUID restaurantId;
    private UUID productId;

}
