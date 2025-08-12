package com.leonardoterrao.ordering.system.order.service.messaging.publisher.kafka;

import com.leonardoterrao.ordering.system.kafka.order.avro.model.PaymentRequestAvroModel;
import com.leonardoterrao.ordering.system.kafka.producer.service.KafkaProducer;
import com.leonardoterrao.ordering.system.order.service.domain.config.OrderServiceConfigData;
import com.leonardoterrao.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.leonardoterrao.ordering.system.order.service.domain.ports.output.message.publisher.payment.OrderCreationPaymentRequestMessagePublisher;
import com.leonardoterrao.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateOrderKafkaMessagePublisher implements OrderCreationPaymentRequestMessagePublisher {

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final OrderServiceConfigData orderServiceConfigData;
    private final KafkaProducer<String, PaymentRequestAvroModel> kafkaProducer;
    private final OrderKafkaMessageHelper orderKafkaMessageHelper;

    @Override
    public void publish(final OrderCreatedEvent domainEvent) {
        final String orderId = domainEvent.getOrder().getId().toString();
        log.info("Received OrderCreatedEvent for order id: {}", orderId);

        try {
            final PaymentRequestAvroModel avroModel = orderMessagingDataMapper
                    .orderCreateEventToPaymentRequestAvroModel(domainEvent);

            kafkaProducer.send(
                    orderServiceConfigData.getPaymentRequestTopicName(),
                    orderId,
                    avroModel,
                    orderKafkaMessageHelper
                            .getKafkaCallback(
                                    orderServiceConfigData.getPaymentResponseTopicName(),
                                    avroModel,
                                    orderId,
                                    PaymentRequestAvroModel.class.getSimpleName()));

            log.info("PaymentRequestAvroModel sent to Kafka for order id: {}", orderId);
        } catch (final Exception e) {
            log.error("Error while processing OrderCreatedEvent for order id: {}", orderId, e);
        }
    }

}
