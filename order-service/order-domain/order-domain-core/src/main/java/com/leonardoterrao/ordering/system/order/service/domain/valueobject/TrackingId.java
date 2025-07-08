package com.leonardoterrao.ordering.system.order.service.domain.valueobject;

import com.leonardoterrao.ordering.system.domain.valueobject.BaseId;

import java.util.UUID;

public class TrackingId extends BaseId<UUID> {

    protected TrackingId(final UUID value) {
        super(value);
    }
    
}
