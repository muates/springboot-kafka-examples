package com.muates.e.commerce.event.service.impl;

import com.muates.e.commerce.event.config.data.KafkaConfigData;
import com.muates.e.commerce.event.model.avro.ECommerceEventAvroModel;
import com.muates.e.commerce.event.model.dto.request.ECommerceEventRequest;
import com.muates.e.commerce.event.service.ECommerceEventService;
import com.muates.e.commerce.event.service.kafka.producer.KafkaProducer;
import com.muates.e.commerce.event.transformer.ECommerceEventRequestToAvroModelTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ECommerceEventServiceImpl implements ECommerceEventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ECommerceEventServiceImpl.class);

    private final KafkaConfigData kafkaConfigData;
    private final KafkaProducer<Long, ECommerceEventAvroModel> kafkaProducer;
    private final ECommerceEventRequestToAvroModelTransformer transformer;

    public ECommerceEventServiceImpl(KafkaConfigData kafkaConfigData,
                                     KafkaProducer<Long, ECommerceEventAvroModel> kafkaProducer,
                                     ECommerceEventRequestToAvroModelTransformer transformer) {
        this.kafkaConfigData = kafkaConfigData;
        this.kafkaProducer = kafkaProducer;
        this.transformer = transformer;
    }

    @Override
    public String sendMessage(ECommerceEventRequest request) {
        ECommerceEventAvroModel eCommerceEventAvroModel = transformer.getAvroModel(request);
        kafkaProducer.send(kafkaConfigData.getTopicName(), eCommerceEventAvroModel.getUserId(), eCommerceEventAvroModel);
        LOGGER.info("Message sent to Kafka for user ID: {}", eCommerceEventAvroModel.getUserId());
        return "Message sent successfully";
    }
}
