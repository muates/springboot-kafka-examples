package com.muates.e.commerce.event.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ECommerceEventRequest {
    private String eventId;
    private String eventType;
    private Long timestamp;
    private String productId;
    private String productName;
    private Long userId;
}
