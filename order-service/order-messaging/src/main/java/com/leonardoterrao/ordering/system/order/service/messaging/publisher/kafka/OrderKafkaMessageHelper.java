package com.leonardoterrao.ordering.system.order.service.messaging.publisher.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@Component
public class OrderKafkaMessageHelper {

    public <T> ListenableFutureCallback<SendResult<String, T>>
    getKafkaCallback(final String responseTopicName, final T requestAvroModel, final String orderId, final String requestAvroModelName) {
        return new ListenableFutureCallback<SendResult<String, T>>() {
            @Override
            public void onFailure(final Throwable ex) {
                log.error("Error sending {} message {} to Kafka topic: {}",
                        requestAvroModelName, requestAvroModel.toString(), responseTopicName, ex);
            }

            @Override
            public void onSuccess(final SendResult<String, T> result) {
                final RecordMetadata recordMetadata = result.getRecordMetadata();
                log.info("Received successful response from Kafka for order id: {} " +
                                " Topic: {} Offset: {} and Partition: {} Timestamp: {}",
                        orderId,
                        recordMetadata.topic(),
                        recordMetadata.offset(),
                        recordMetadata.partition(),
                        recordMetadata.timestamp());
            }
        };
    }

}
