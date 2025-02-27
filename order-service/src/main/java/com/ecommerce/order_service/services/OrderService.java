package com.ecommerce.order_service.services;


import com.ecommerce.order_service.dto.enums.OrderStatus;
import com.ecommerce.order_service.dto.request.OrderRequest;
import com.ecommerce.order_service.dto.response.OrderResponse;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    // Handle order creation
    public OrderResponse placeAnOrder(OrderRequest orderRequest) {

        return new OrderResponse(orderRequest.getOrderId(), OrderStatus.CREATED);
    }

    // Handle order confirmation
    public OrderResponse confirmOrder(String orderId) {

        return new OrderResponse(orderId, OrderStatus.CONFIRMED);
    }

    private void saveAndPublishEvents(Object orderEvent) {

    }


}