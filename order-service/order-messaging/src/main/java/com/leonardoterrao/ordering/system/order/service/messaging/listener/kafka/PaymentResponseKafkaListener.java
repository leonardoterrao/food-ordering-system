package com.leonardoterrao.ordering.system.order.service.messaging.listener.kafka;

import com.leonardoterrao.ordering.system.kafka.consumer.KafkaConsumer;
import com.leonardoterrao.ordering.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.leonardoterrao.ordering.system.kafka.order.avro.model.PaymentStatus;
import com.leonardoterrao.ordering.system.order.service.domain.ports.input.message.listener.payment.PaymentResponseMessageListener;
import com.leonardoterrao.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentResponseKafkaListener implements KafkaConsumer<PaymentResponseAvroModel> {

    private final PaymentResponseMessageListener paymentResponseMessageListener;
    private final OrderMessagingDataMapper orderMessagingDataMapper;

    @Override
    @KafkaListener(id = "${kafka-consumer-config.payment-consumer-group-id}",
            topics = "${order-service.payment-response-topic-name}")
    public void receive(@Payload final List<PaymentResponseAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY) final List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) final List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) final List<Long> offsets) {


        log.info("{} number of payment response messages received with keys: {}, partitions: {} and offsets: {}",
                messages.size(), keys, partitions, offsets);

        messages.forEach(p -> {
            if (PaymentStatus.COMPLETED == p.getPaymentStatus()) {
                log.info("Processing successful payment for order id: {}", p.getOrderId());
                paymentResponseMessageListener.paymentCompleted(
                        orderMessagingDataMapper.paymentResponseAvroModelToPaymentResponse(p));
            } else if (PaymentStatus.FAILED == p.getPaymentStatus() || PaymentStatus.CANCELLED == p.getPaymentStatus()) {
                log.info("Processing unsuccessful payment for order id: {}", p.getOrderId());
                paymentResponseMessageListener.paymentCancelled(
                        orderMessagingDataMapper.paymentResponseAvroModelToPaymentResponse(p));
            }
        });
    }

}
