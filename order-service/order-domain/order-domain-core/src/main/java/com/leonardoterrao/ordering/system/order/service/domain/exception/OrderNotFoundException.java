package com.leonardoterrao.ordering.system.order.service.domain.exception;

import com.leonardoterrao.ordering.system.domain.exception.DomainException;

public class OrderNotFoundException extends DomainException {

    public OrderNotFoundException(final String message) {
        super(message);
    }

    public OrderNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
