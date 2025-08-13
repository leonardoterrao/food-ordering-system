package com.leonardoterrao.ordering.system.order.service.messaging.listener.kafka;

import com.leonardoterrao.ordering.system.kafka.consumer.KafkaConsumer;
import com.leonardoterrao.ordering.system.kafka.order.avro.model.OrderApprovalStatus;
import com.leonardoterrao.ordering.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.leonardoterrao.ordering.system.order.service.domain.ports.input.message.listener.restauranteapproval.RestaurantApprovalResponseMessageListener;
import com.leonardoterrao.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.leonardoterrao.ordering.system.order.service.domain.entity.Order.FAILURE_MESSAGE_DELIMITER;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestaurantApprovalResponseKafkaListener implements KafkaConsumer<RestaurantApprovalResponseAvroModel> {

    private final RestaurantApprovalResponseMessageListener restaurantApprovalResponseMessageListener;
    private final OrderMessagingDataMapper orderMessagingDataMapper;

    @Override
    @KafkaListener(id = "${kafka-consumer-config.restaurant-approval-consumer-group-id}",
            topics = "${order-service.restaurant-approval-response-topic-name}")
    public void receive(@Payload final List<RestaurantApprovalResponseAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY) final List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) final List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) final List<Long> offsets) {
        log.info("{} number of restaurant approval response messages received with keys: {}, partitions: {} and offsets: {}",
                messages.size(), keys, partitions, offsets);

        messages.forEach(r -> {
            if (OrderApprovalStatus.APPROVED == r.getOrderApprovalStatus()) {
                log.info("Processing approved restaurant order for order id: {}", r.getOrderId());
                restaurantApprovalResponseMessageListener.orderApproved(
                        orderMessagingDataMapper.approvalResponseAvroModelToRestaurantApprovalResponse(r));
            } else if (OrderApprovalStatus.REJECTED == r.getOrderApprovalStatus()) {
                log.info("Processing rejected restaurant order for order id: {}, with failure messages: {}",
                        r.getOrderId(), String.join(FAILURE_MESSAGE_DELIMITER, r.getFailureMessages()));
                restaurantApprovalResponseMessageListener.orderRejected(
                        orderMessagingDataMapper.approvalResponseAvroModelToRestaurantApprovalResponse(r));
            }
        });
    }
}
