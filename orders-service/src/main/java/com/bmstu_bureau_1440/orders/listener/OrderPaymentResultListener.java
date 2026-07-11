package com.bmstu_bureau_1440.orders.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.bmstu_bureau_1440.orders.service.OrderProcessingService;
import com.bmstu_bureau_1440.shared.event.OrderPaymentResultEvent;
import com.bmstu_bureau_1440.shared.event.PaymentTopics;

import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class OrderPaymentResultListener {

    private final OrderProcessingService orderProcessingService;

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = PaymentTopics.ORDER_PAYMENT_RESULT)
    public void onPaymentResult(String payload) {
        OrderPaymentResultEvent event = objectMapper.readValue(payload, OrderPaymentResultEvent.class);

        orderProcessingService.handleOrderPaymentResult(event);
    }

}
