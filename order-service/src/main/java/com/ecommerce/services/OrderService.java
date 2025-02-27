package com.ecommerce.services;


import com.ecommerce.dto.enums.OrderStatus;
import com.ecommerce.dto.request.OrderRequest;
import com.ecommerce.dto.response.OrderResponse;
import com.ecommerce.entity.OrderEvent;
import com.ecommerce.publisher.OrderEventKafkaPublisher;
import com.ecommerce.repository.OrderEventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @Autowired
    private OrderEventRepository repository;

    @Autowired
    private OrderEventKafkaPublisher publisher;

    // Handle order creation
    public OrderResponse placeAnOrder(OrderRequest orderRequest) {
        String orderId = UUID.randomUUID().toString().split("-")[0];
        orderRequest.setOrderId(orderId);

        //TODO: Logique métier ici

        OrderEvent orderEvent = new OrderEvent(orderId, OrderStatus.CREATED, "Order created successfully", LocalDateTime.now());

        try {
            String orderEventJson = objectMapper.writeValueAsString(orderEvent);
            LOGGER.info("Order Event Created: {}", orderEventJson);
        } catch (Exception e) {
            LOGGER.error("Error while serializing order event: {}", e.getMessage());
        }

        saveAndPublishEvents(orderEvent, orderId);

        return new OrderResponse(orderId, OrderStatus.CREATED);
    }

    // Handle order confirmation
    public OrderResponse confirmOrder(String orderId) {
        OrderEvent orderEvent = new OrderEvent(orderId, OrderStatus.CONFIRMED, "Order confirmed successfully", LocalDateTime.now());
        saveAndPublishEvents(orderEvent, orderId);
        return new OrderResponse(orderId, OrderStatus.CONFIRMED);
    }

    private void saveAndPublishEvents(OrderEvent orderEvent, String orderId) {
        // Save to Mongo
        repository.save(orderEvent);
        LOGGER.info("Order Event Saved: {}", orderId);

        // Publish to Kafka
        publisher.sendOrderEvent(orderEvent);
        LOGGER.info("Order Event Published to Kafka: {}", orderId);
    }
}