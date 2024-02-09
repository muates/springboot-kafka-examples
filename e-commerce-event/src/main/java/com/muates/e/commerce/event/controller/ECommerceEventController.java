package com.muates.e.commerce.event.controller;

import com.muates.e.commerce.event.model.dto.request.ECommerceEventRequest;
import com.muates.e.commerce.event.service.ECommerceEventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/commerce/event")
public class ECommerceEventController {

    private final ECommerceEventService eCommerceEventService;

    public ECommerceEventController(ECommerceEventService eCommerceEventService) {
        this.eCommerceEventService = eCommerceEventService;
    }

    @PostMapping
    public ResponseEntity<String> sendMessage(@RequestBody ECommerceEventRequest request) {
        return ResponseEntity.ok(eCommerceEventService.sendMessage(request));
    }
}
