package com.bmstu_bureau_1440.orders.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bmstu_bureau_1440.orders.dto.CreateOrderRequest;
import com.bmstu_bureau_1440.orders.model.Order;
import com.bmstu_bureau_1440.orders.service.OrderService;
import com.bmstu_bureau_1440.shared.web.UserIdHeaderInterceptor;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(OrdersApi.BASE_PATH)
@RequiredArgsConstructor
public class OrdersController {

    private final OrderService orderService;

    @GetMapping
    public List<Order> getOrders(@RequestAttribute(UserIdHeaderInterceptor.USER_ID_ATTRIBUTE) String userId) {
        return orderService.findAll(userId);
    }

    @GetMapping(OrdersApi.ORDER_ID_PATH)
    public Order getOrder(
            @PathVariable(OrdersApi.ORDER_ID_PARAM) String orderId,
            @RequestAttribute(UserIdHeaderInterceptor.USER_ID_ATTRIBUTE) String userId) {
        return orderService.findById(orderId, userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order createOrder(
            @RequestAttribute(UserIdHeaderInterceptor.USER_ID_ATTRIBUTE) String userId,
            @RequestBody @Validated CreateOrderRequest request) {
        return orderService.create(userId, request);
    }

}
