package com.bmstu_bureau_1440.orders.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.bmstu_bureau_1440.orders.dto.CreateOrderRequest;
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

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order findById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ORDER_NOT_FOUND"));
    }

    @Transactional
    public Order create(CreateOrderRequest request) {
        return orderRepository.save(payloadMapperRegistry.map(request.payload()));
    }

}
