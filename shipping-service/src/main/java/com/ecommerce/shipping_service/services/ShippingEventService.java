package com.ecommerce.shipping_service.services;

import org.springframework.stereotype.Service;

@Service
public class ShippingEventService {

    public void consumeOrderEvent(Object orderEvent) {

    }

    // Ship the order
    public void shipOrder(String orderId) {

    }

    // Deliver the order
    public void deliverOrder(String orderId) {

    }

    private void saveAndPublishShippingEvent(Object event) {

    }
}