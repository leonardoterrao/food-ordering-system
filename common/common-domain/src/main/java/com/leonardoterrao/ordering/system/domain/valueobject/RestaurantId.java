package com.leonardoterrao.ordering.system.domain.valueobject;

import java.util.UUID;

public class RestaurantId extends BaseId<UUID> {

    protected RestaurantId(final UUID value) {
        super(value);
    }

}
