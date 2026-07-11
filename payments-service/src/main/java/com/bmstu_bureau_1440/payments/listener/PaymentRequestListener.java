package com.bmstu_bureau_1440.payments.listener;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.bmstu_bureau_1440.payments.service.PaymentProcessingService;
import com.bmstu_bureau_1440.shared.event.OrderPaymentRequestedEvent;
import com.bmstu_bureau_1440.shared.event.PaymentTopics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentRequestListener {

    private final PaymentProcessingService paymentProcessingService;

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = PaymentTopics.ORDER_PAYMENT_REQUESTED)
    public void onPaymentRequested(String payload) {
        OrderPaymentRequestedEvent event = objectMapper.readValue(payload, OrderPaymentRequestedEvent.class);

        try {
            paymentProcessingService.processOrderPaymentRequest(event);
        } catch (DataIntegrityViolationException e) {
            log.info("Lost race processing OrderPaymentRequested for order {}, already handled concurrently",
                    event.orderId());
        }
    }

}
