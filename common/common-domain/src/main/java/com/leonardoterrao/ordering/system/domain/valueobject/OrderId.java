package com.leonardoterrao.ordering.system.domain.valueobject;

import java.util.UUID;

public class OrderId extends BaseId<UUID> {

    protected OrderId(final UUID value) {
        super(value);
    }

}
