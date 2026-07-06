package com.bmstu_bureau_1440.orders.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bmstu_bureau_1440.orders.dto.CreateOrderRequest;
import com.bmstu_bureau_1440.orders.model.Order;
import com.bmstu_bureau_1440.orders.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(OrdersApi.BASE_PATH)
@RequiredArgsConstructor
public class OrdersController {

    private final OrderService orderService;

    @GetMapping
    public List<Order> getOrders() {
        return orderService.findAll();
    }

    @GetMapping(OrdersApi.ORDER_ID_PATH)
    public Order getOrder(@PathVariable(OrdersApi.ORDER_ID_PARAM) UUID orderId) {
        return orderService.findById(orderId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order createOrder(@RequestBody @Validated CreateOrderRequest request) {
        return orderService.create(request);
    }

}
