package com.leonardoterrao.ordering.system.order.service.messaging.publisher.kafka;

import com.leonardoterrao.ordering.system.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.leonardoterrao.ordering.system.kafka.producer.service.KafkaProducer;
import com.leonardoterrao.ordering.system.order.service.domain.config.OrderServiceConfigData;
import com.leonardoterrao.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.leonardoterrao.ordering.system.order.service.domain.ports.output.message.publisher.restaurantapproval.OrderPaidRestaurantRequestMessagePublisher;
import com.leonardoterrao.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PayOrderKafkaMessagePublisher implements OrderPaidRestaurantRequestMessagePublisher {

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final OrderServiceConfigData orderServiceConfigData;
    private final KafkaProducer <String, RestaurantApprovalRequestAvroModel> kafkaProducer;
    private final OrderKafkaMessageHelper orderKafkaMessageHelper;

    @Override
    public void publish(final OrderPaidEvent domainEvent) {
        final String orderId = domainEvent.getOrder().getId().toString();

        try {
            final RestaurantApprovalRequestAvroModel avroModel = orderMessagingDataMapper
                    .orderPaidEventToRestaurantApprovalRequestAvroModel(domainEvent);

            kafkaProducer.send(orderServiceConfigData.getRestaurantApprovalRequestTopicName(),
                    orderId,
                    avroModel,
                    orderKafkaMessageHelper.getKafkaCallback(
                            orderServiceConfigData.getRestaurantApprovalRequestTopicName(),
                            avroModel,
                            orderId,
                            RestaurantApprovalRequestAvroModel.class.getSimpleName()));

            log.info("RestaurantApprovalRequestAvroModel sent to Kafka for order id: {}", orderId);
        } catch (final Exception e) {
            log.error("Error while processing OrderPaidEvent for order id: {}", orderId, e);
        }


    }

}
