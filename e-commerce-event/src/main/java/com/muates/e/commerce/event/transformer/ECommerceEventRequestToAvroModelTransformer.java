package com.muates.e.commerce.event.transformer;

import com.muates.e.commerce.event.model.avro.ECommerceEventAvroModel;
import com.muates.e.commerce.event.model.dto.request.ECommerceEventRequest;
import org.springframework.stereotype.Component;

@Component
public class ECommerceEventRequestToAvroModelTransformer {

    public ECommerceEventAvroModel getAvroModel(ECommerceEventRequest request) {
        return ECommerceEventAvroModel
                .newBuilder()
                .setEventId(request.getEventId())
                .setTimestamp(request.getTimestamp())
                .setEventType(request.getEventType())
                .setProductId(request.getProductId())
                .setProductName(request.getProductName())
                .setUserId(request.getUserId())
                .build();
    }
}
