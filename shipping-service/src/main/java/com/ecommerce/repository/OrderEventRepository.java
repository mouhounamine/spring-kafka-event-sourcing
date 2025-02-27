package com.ecommerce.repository;

import com.ecommerce.entity.OrderEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderEventRepository extends MongoRepository<OrderEvent, String> {
}
