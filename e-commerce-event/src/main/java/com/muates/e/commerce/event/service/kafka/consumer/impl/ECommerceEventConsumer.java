package com.muates.e.commerce.event.service.kafka.consumer.impl;

import com.muates.e.commerce.event.config.data.KafkaConfigData;
import com.muates.e.commerce.event.config.data.KafkaConsumerConfigData;
import com.muates.e.commerce.event.model.avro.ECommerceEventAvroModel;
import com.muates.e.commerce.event.service.kafka.consumer.KafkaConsumer;
import com.muates.e.commerce.event.util.TopicChecker;
import org.apache.kafka.clients.admin.AdminClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ECommerceEventConsumer implements KafkaConsumer<Long, ECommerceEventAvroModel> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ECommerceEventConsumer.class);

    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
    private final KafkaConfigData kafkaConfigData;
    private final KafkaConsumerConfigData kafkaConsumerConfigData;
    private final AdminClient adminClient;

    public ECommerceEventConsumer(KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry,
                                  KafkaConfigData kafkaConfigData,
                                  KafkaConsumerConfigData kafkaConsumerConfigData,
                                  @Qualifier("adminClient") AdminClient adminClient) {
        this.kafkaListenerEndpointRegistry = kafkaListenerEndpointRegistry;
        this.kafkaConfigData = kafkaConfigData;
        this.kafkaConsumerConfigData = kafkaConsumerConfigData;
        this.adminClient = adminClient;
    }

    @EventListener
    public void onAppStarted(ApplicationStartedEvent event) {
        TopicChecker.checkTopic(adminClient, kafkaConfigData.getTopicName());
        LOGGER.info("Topic with name {} is ready for operations!", kafkaConfigData.getTopicNamesToCreate().toArray());
        Objects.requireNonNull(kafkaListenerEndpointRegistry
                .getListenerContainer(kafkaConsumerConfigData.getConsumerGroupId())).start();
    }

    @Override
    @KafkaListener(id = "${kafka-consumer-config.consumer-group-id}", topics = "${kafka-config.topic-name}")
    public void receive(
            @Payload List<ECommerceEventAvroModel> messages,
            @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<Integer> keys,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
            @Header(KafkaHeaders.OFFSET) List<Long> offsets
    ) {
        LOGGER.info("Received Kafka Message - Messages: {}, Keys: {}, Partitions: {}, Offsets: {}",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString());
    }
}
