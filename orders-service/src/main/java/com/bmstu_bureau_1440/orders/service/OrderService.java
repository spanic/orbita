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
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;

    private final PayloadMapperRegistry payloadMapperRegistry;

    private final OrderProcessingService orderProcessingService;

    public List<Order> findAll(String userId) {
        return orderRepository.findAllByUserId(userId);
    }

    public Order findById(String orderId, String userId) {
        UUID id;

        try {
            id = UUID.fromString(orderId);
        } catch (IllegalArgumentException e) {
            throw new OrderNotFoundException();
        }

        return orderRepository.findByIdAndUserId(id, userId).orElseThrow(OrderNotFoundException::new);
    }

    @Transactional
    public Order create(String userId, CreateOrderRequest request) {
        Order order = orderRepository.save(payloadMapperRegistry.map(userId, request.payload()));

        orderProcessingService.enqueueOrderPaymentRequest(order);

        return order;
    }

}
