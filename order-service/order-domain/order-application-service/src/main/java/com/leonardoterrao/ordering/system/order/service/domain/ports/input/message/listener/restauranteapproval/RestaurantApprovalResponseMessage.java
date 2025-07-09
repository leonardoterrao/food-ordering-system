package com.leonardoterrao.ordering.system.order.service.domain.ports.input.message.listener.restauranteapproval;

import com.leonardoterrao.ordering.system.order.service.domain.dto.message.RestaurantApprovalResponse;

public interface RestaurantApprovalResponseMessage {

    void orderApproved(RestaurantApprovalResponse restaurantApprovalResponse);

    void orderRejected(RestaurantApprovalResponse restaurantApprovalResponse);

}
