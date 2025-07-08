package com.leonardoterrao.ordering.system.order.service.domain.valueobject;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = "id")
public class StreetAddress {

    private final UUID id;
    private final String street;
    private final String postalCode;
    private final String city;

}
