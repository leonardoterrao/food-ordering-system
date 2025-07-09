package com.leonardoterrao.ordering.system.order.service.domain.ports.input.message.listener.payment;

import com.leonardoterrao.ordering.system.order.service.domain.dto.message.PaymentResponse;

public interface PaymentResponseMessageListener {

    void paymentCompleted(PaymentResponse paymentResponseMessage);

    void paymentCancelled(PaymentResponse paymentResponseMessage);

}
