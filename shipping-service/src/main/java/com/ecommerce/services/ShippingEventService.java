package com.ecommerce.services;

import com.ecommerce.dto.enums.OrderStatus;
import com.ecommerce.entity.OrderEvent;
import com.ecommerce.repository.OrderEventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ShippingEventService {
    @Autowired
    private OrderEventRepository orderEventRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ShippingEventService.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "${shipping.event.topicName}", groupId = "shipping-service")
    public void consumeOrderEvent(OrderEvent orderEvent) {

        LOGGER.info("About to consume orderEvent with orderEventId: '{}', orderId: '{}', status: '{}', details: '{}', timeStamp: '{}'", orderEvent.getId(), orderEvent.getOrderId(), orderEvent.getStatus(), orderEvent.getDetails(), orderEvent.getEventTimestamp());
        if (orderEvent.getStatus() == OrderStatus.CONFIRMED) {
            // Auto ship after order confirmation
            shipOrder(orderEvent.getOrderId());
        }
    }

    // Ship the order
    public void shipOrder(String orderId) {
        OrderEvent orderEvent = new OrderEvent(orderId, OrderStatus.SHIPPED, "Order shipped successfully", LocalDateTime.now());
        LOGGER.info("About to ship order with orderEventId: '{}', orderId: '{}', status: '{}', details: '{}', timeStamp: '{}'", orderEvent.getId(), orderEvent.getOrderId(), orderEvent.getStatus(), orderEvent.getDetails(), orderEvent.getEventTimestamp());
        orderEventRepository.save(orderEvent);
    }

    // Deliver the order
    public void deliverOrder(String orderId) {
        LOGGER.info("Order delivered successfully with orderId: '{}'", orderId);
        OrderEvent orderEvent = new OrderEvent(orderId, OrderStatus.DELIVERED, "Order delivered successfully", LocalDateTime.now());
        orderEventRepository.save(orderEvent);
    }

    private void saveAndPublishShippingEvent(Object event) {

    }
}