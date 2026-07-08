package com.bmstu_bureau_1440.orders.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bmstu_bureau_1440.orders.dto.CreateOrderRequest;
import com.bmstu_bureau_1440.orders.error.OrderNotFoundException;
import com.bmstu_bureau_1440.orders.mapper.PayloadMapperRegistry;
import com.bmstu_bureau_1440.orders.model.Order;
import com.bmstu_bureau_1440.orders.model.OrderStatus;
import com.bmstu_bureau_1440.orders.repository.OrderRepository;
import com.bmstu_bureau_1440.shared.event.OrderPaymentRequestedEvent;
import com.bmstu_bureau_1440.shared.event.OrderPaymentResultEvent;
import com.bmstu_bureau_1440.shared.event.PaymentTopics;
import com.bmstu_bureau_1440.shared.outbox.OutboxEventWriter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;

    private final PayloadMapperRegistry payloadMapperRegistry;

    private final OutboxEventWriter outboxEventWriter;

    private final ObjectMapper objectMapper;

    public List<Order> findAll(String userId) {
        return orderRepository.findAllByUserId(userId);
    }

    public Order findById(UUID orderId, String userId) {
        return orderRepository.findByIdAndUserId(orderId, userId).orElseThrow(OrderNotFoundException::new);
    }

    @Transactional
    public Order create(String userId, CreateOrderRequest request) {
        Order order = orderRepository.save(payloadMapperRegistry.map(userId, request.payload()));

        outboxEventWriter.enqueue(
                order.getId(),
                "ORDER_PAYMENT_REQUESTED",
                PaymentTopics.ORDER_PAYMENT_REQUESTED,
                new OrderPaymentRequestedEvent(
                        UUID.randomUUID(),
                        order.getId(),
                        order.getUserId(),
                        order.getPrice(),
                        Instant.now()));

        return order;
    }

    @KafkaListener(topics = PaymentTopics.ORDER_PAYMENT_RESULT)
    @Transactional
    public void onPaymentResult(String payload) {
        OrderPaymentResultEvent event = objectMapper.readValue(payload, OrderPaymentResultEvent.class);

        switch (event.outcome()) {
            case COMPLETED -> applyPaymentCompleted(event);
            case FAILED -> applyPaymentFailed(event);
        }
    }

    @Transactional
    public void applyPaymentCompleted(OrderPaymentResultEvent event) {
        orderRepository.findById(event.orderId()).ifPresentOrElse(order -> {
            if (order.getStatus() == OrderStatus.PAYMENT_PENDING) {
                order.setStatus(OrderStatus.PAID);
            } else {
                log.info("Ignoring duplicate OrderPaymentCompleted for order {} (status={})",
                        event.orderId(), order.getStatus());
            }
        }, () -> log.warn("OrderPaymentCompleted for unknown order {}", event.orderId()));
    }

    @Transactional
    public void applyPaymentFailed(OrderPaymentResultEvent event) {
        orderRepository.findById(event.orderId()).ifPresentOrElse(order -> {
            if (order.getStatus() == OrderStatus.PAYMENT_PENDING) {
                order.setStatus(OrderStatus.PAYMENT_FAILED);
            } else {
                log.info("Ignoring duplicate OrderPaymentFailed for order {} (status={})",
                        event.orderId(), order.getStatus());
            }
        }, () -> log.warn("OrderPaymentFailed for unknown order {}", event.orderId()));
    }

}
