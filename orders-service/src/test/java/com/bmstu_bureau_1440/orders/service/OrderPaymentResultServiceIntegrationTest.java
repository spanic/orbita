package com.bmstu_bureau_1440.orders.service;

import static com.bmstu_bureau_1440.orders.OrderTestsFixtures.ARCHIVE_ORDER_MODEL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.bmstu_bureau_1440.orders.TestContainersConfiguration;
import com.bmstu_bureau_1440.orders.model.Order;
import com.bmstu_bureau_1440.orders.model.OrderStatus;
import com.bmstu_bureau_1440.orders.repository.OrderRepository;
import com.bmstu_bureau_1440.shared.event.OrderPaymentResultEvent;
import com.bmstu_bureau_1440.shared.event.PaymentFailureReason;
import com.bmstu_bureau_1440.shared.event.PaymentResultOutcome;

import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@Import(TestContainersConfiguration.class)
class OrderPaymentResultServiceIntegrationTest {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderProcessingService orderProcessingService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void cleanUp() {
        orderRepository.deleteAll();
    }

    @ParameterizedTest
    @EnumSource(PaymentResultOutcome.class)
    void applyPaymentResult_marksPendingOrderAccordingToOutcome(PaymentResultOutcome outcome) {
        Order order = orderRepository.save(pendingOrder());

        orderProcessingService.handleOrderPaymentResult(eventFor(order, outcome));

        assertThat(orderRepository.findById(order.getId()).orElseThrow().getStatus())
                .isEqualTo(outcome == PaymentResultOutcome.COMPLETED ? OrderStatus.PAID : OrderStatus.PAYMENT_FAILED);
    }

    @Test
    void applyPaymentCompleted_isIdempotent_whenOrderAlreadyPaid() {
        Order order = orderRepository.save(pendingOrder());

        orderProcessingService.handleOrderPaymentResult(eventFor(order, PaymentResultOutcome.COMPLETED));
        orderProcessingService.handleOrderPaymentResult(eventFor(order, PaymentResultOutcome.COMPLETED));

        assertThat(orderRepository.findById(order.getId()).orElseThrow().getStatus())
                .isEqualTo(OrderStatus.PAID);
    }

    @Test
    void applyPaymentCompleted_doesNotOverridePaymentFailed() {
        Order order = orderRepository.save(pendingOrder());

        orderProcessingService.handleOrderPaymentResult(eventFor(order, PaymentResultOutcome.FAILED));
        orderProcessingService.handleOrderPaymentResult(eventFor(order, PaymentResultOutcome.COMPLETED));

        assertThat(orderRepository.findById(order.getId()).orElseThrow().getStatus())
                .isEqualTo(OrderStatus.PAYMENT_FAILED);
    }

    @Test
    void applyPaymentCompleted_doesNotThrow_whenOrderDoesNotExist() {
        orderProcessingService.handleOrderPaymentResult(Instancio.of(OrderPaymentResultEvent.class)
                .set(field(OrderPaymentResultEvent::outcome), PaymentResultOutcome.COMPLETED)
                .set(field(OrderPaymentResultEvent::failureReason), null)
                .create());
    }

    private static Order pendingOrder() {
        return Instancio.of(ARCHIVE_ORDER_MODEL)
                .set(field(Order::getStatus), OrderStatus.PAYMENT_PENDING)
                .create();
    }

    private static OrderPaymentResultEvent eventFor(Order order, PaymentResultOutcome outcome) {
        return Instancio.of(OrderPaymentResultEvent.class)
                .set(field(OrderPaymentResultEvent::orderId), order.getId())
                .set(field(OrderPaymentResultEvent::userId), order.getUserId())
                .set(field(OrderPaymentResultEvent::outcome), outcome)
                .set(field(OrderPaymentResultEvent::failureReason),
                        outcome == PaymentResultOutcome.FAILED ? PaymentFailureReason.INSUFFICIENT_BALANCE : null)
                .create();
    }

}
