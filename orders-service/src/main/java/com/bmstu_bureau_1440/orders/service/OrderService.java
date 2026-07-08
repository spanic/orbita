package com.bmstu_bureau_1440.orders.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bmstu_bureau_1440.orders.dto.CreateOrderRequest;
import com.bmstu_bureau_1440.orders.error.OrderNotFoundException;
import com.bmstu_bureau_1440.orders.mapper.PayloadMapperRegistry;
import com.bmstu_bureau_1440.orders.model.Order;
import com.bmstu_bureau_1440.orders.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final PayloadMapperRegistry payloadMapperRegistry;

    public List<Order> findAll(String userId) {
        return orderRepository.findAllByUserId(userId);
    }

    public Order findById(UUID orderId, String userId) {
        return orderRepository.findByIdAndUserId(orderId, userId).orElseThrow(OrderNotFoundException::new);
    }

    @Transactional
    public Order create(String userId, CreateOrderRequest request) {
        return orderRepository.save(payloadMapperRegistry.map(userId, request.payload()));
    }

}
