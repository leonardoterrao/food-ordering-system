package com.leonardoterrao.ordering.system.order.service.domain;

import com.leonardoterrao.ordering.system.order.service.domain.dto.message.RestaurantApprovalResponse;
import com.leonardoterrao.ordering.system.order.service.domain.ports.input.message.listener.restauranteapproval.RestaurantApprovalResponseMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated
@Service
public class RestaurantApprovalResponseMessageListenerListenerImpl implements RestaurantApprovalResponseMessageListener {

    @Override
    public void orderApproved(final RestaurantApprovalResponse restaurantApprovalResponse) {

    }

    @Override
    public void orderRejected(final RestaurantApprovalResponse restaurantApprovalResponse) {

    }
}
