package com.bmstu_bureau_1440.orders.service;

import com.bmstu_bureau_1440.orders.dto.CreateOrderRequest;
import com.bmstu_bureau_1440.orders.model.Order;
import com.bmstu_bureau_1440.orders.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order create(CreateOrderRequest request) {
        return orderRepository.save(request.payload().toEntity());
    }

}
