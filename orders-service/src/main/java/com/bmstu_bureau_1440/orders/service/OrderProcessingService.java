package com.bmstu_bureau_1440.orders.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bmstu_bureau_1440.orders.model.Order;
import com.bmstu_bureau_1440.orders.model.OrderStatus;
import com.bmstu_bureau_1440.orders.repository.OrderRepository;
import com.bmstu_bureau_1440.shared.event.OrderPaymentRequestedEvent;
import com.bmstu_bureau_1440.shared.event.OrderPaymentResultEvent;
import com.bmstu_bureau_1440.shared.event.PaymentTopics;
import com.bmstu_bureau_1440.shared.outbox.OutboxEventWriter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderProcessingService {

    private final OrderRepository orderRepository;

    private final OutboxEventWriter outboxEventWriter;

    public Order enqueueOrderPaymentRequest(Order order) {
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

    @Transactional
    public void handleOrderPaymentResult(OrderPaymentResultEvent event) {
        orderRepository.findById(event.orderId()).ifPresentOrElse(order -> {

            if (order.getStatus() == OrderStatus.PAYMENT_PENDING) {
                switch (event.outcome()) {
                    case COMPLETED -> {
                        order.setStatus(OrderStatus.PAID);
                    }
                    case FAILED -> {
                        order.setFailureReason(event.failureReason());
                        order.markPaymentFailed();
                    }
                }
            } else {
                log.info("Ignoring duplicate OrderPaymentResult for order {} (status={})", event.orderId(),
                        order.getStatus());
            }

        }, () -> log.warn("OrderPaymentResult for unknown order {}", event.orderId()));
    }

}
