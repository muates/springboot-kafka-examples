package com.muates.e.commerce.event.service;

import com.muates.e.commerce.event.model.dto.request.ECommerceEventRequest;

public interface ECommerceEventService {
    String sendMessage(ECommerceEventRequest request);
}
