package com.muates.e.commerce.event.service.kafka.producer.impl;

import com.muates.e.commerce.event.model.avro.ECommerceEventAvroModel;
import com.muates.e.commerce.event.service.kafka.producer.KafkaProducer;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.PreDestroy;

@Service
public class ECommerceEventProducer implements KafkaProducer<Long, ECommerceEventAvroModel> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ECommerceEventProducer.class);

    private KafkaTemplate<Long, ECommerceEventAvroModel> kafkaTemplate;

    public ECommerceEventProducer(KafkaTemplate<Long, ECommerceEventAvroModel> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(String topicName, Long key, ECommerceEventAvroModel message) {
        LOGGER.info("Sending message='{}' to topic= '{}'", message, topicName);
        ListenableFuture<SendResult<Long, ECommerceEventAvroModel>> result = kafkaTemplate.send(topicName, key, message);
        addCallback(topicName, message, result);
    }

    @PreDestroy
    private void close() {
        if (kafkaTemplate != null) {
            LOGGER.info("Closing kafka producer");
            kafkaTemplate.destroy();
        }
    }

    private static void addCallback(String topicName, ECommerceEventAvroModel message, ListenableFuture<SendResult<Long, ECommerceEventAvroModel>> result) {
        result.addCallback(new ListenableFutureCallback<SendResult<Long, ECommerceEventAvroModel>>() {
            @Override
            public void onFailure(Throwable throwable) {
                LOGGER.error("Error while sending message {} to topic {}", message.toString(), topicName, throwable);
            }

            @Override
            public void onSuccess(SendResult<Long, ECommerceEventAvroModel> sendResult) {
                RecordMetadata metadata =  sendResult.getRecordMetadata();
                LOGGER.debug("Received new metadata. Topic: {}; Partition: {}; Offset: {}; Timestamp {}, at time {}",
                        metadata.topic(),
                        metadata.partition(),
                        metadata.offset(),
                        metadata.timestamp(),
                        System.nanoTime());
            }
        });
    }
}
