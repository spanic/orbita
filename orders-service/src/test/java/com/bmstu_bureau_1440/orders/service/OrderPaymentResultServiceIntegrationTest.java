package com.bmstu_bureau_1440.orders.service;

import static com.bmstu_bureau_1440.orders.OrderTestsFixtures.ARCHIVE_ORDER_MODEL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.bmstu_bureau_1440.orders.TestContainersConfiguration;
import com.bmstu_bureau_1440.orders.model.Order;
import com.bmstu_bureau_1440.orders.model.OrderStatus;
import com.bmstu_bureau_1440.orders.repository.OrderRepository;
import com.bmstu_bureau_1440.shared.event.OrderPaymentCompletedEvent;
import com.bmstu_bureau_1440.shared.event.OrderPaymentFailedEvent;
import com.bmstu_bureau_1440.shared.event.PaymentFailureReason;

import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@Import(TestContainersConfiguration.class)
class OrderPaymentResultServiceIntegrationTest {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void cleanUp() {
        orderRepository.deleteAll();
    }

    @Test
    void onPaymentCompleted_marksPendingOrderAsPaid_whenInvokedViaTheListenerEntryPoint() {
        Order order = orderRepository.save(pendingOrder());

        orderService.onPaymentCompleted(objectMapper.writeValueAsString(completedEventFor(order)));

        assertThat(orderRepository.findById(order.getId()).orElseThrow().getStatus())
                .isEqualTo(OrderStatus.PAID);
    }

    @Test
    void onPaymentFailed_marksPendingOrderAsPaymentFailed_whenInvokedViaTheListenerEntryPoint() {
        Order order = orderRepository.save(pendingOrder());

        orderService.onPaymentFailed(objectMapper.writeValueAsString(failedEventFor(order)));

        assertThat(orderRepository.findById(order.getId()).orElseThrow().getStatus())
                .isEqualTo(OrderStatus.PAYMENT_FAILED);
    }

    @Test
    void applyPaymentCompleted_marksPendingOrderAsPaid() {
        Order order = orderRepository.save(pendingOrder());

        orderService.applyPaymentCompleted(completedEventFor(order));

        assertThat(orderRepository.findById(order.getId()).orElseThrow().getStatus())
                .isEqualTo(OrderStatus.PAID);
    }

    @Test
    void applyPaymentCompleted_isIdempotent_whenOrderAlreadyPaid() {
        Order order = orderRepository.save(pendingOrder());
        orderService.applyPaymentCompleted(completedEventFor(order));

        orderService.applyPaymentCompleted(completedEventFor(order));

        assertThat(orderRepository.findById(order.getId()).orElseThrow().getStatus())
                .isEqualTo(OrderStatus.PAID);
    }

    @Test
    void applyPaymentCompleted_doesNotOverridePaymentFailed() {
        Order order = orderRepository.save(pendingOrder());
        orderService.applyPaymentFailed(failedEventFor(order));

        orderService.applyPaymentCompleted(completedEventFor(order));

        assertThat(orderRepository.findById(order.getId()).orElseThrow().getStatus())
                .isEqualTo(OrderStatus.PAYMENT_FAILED);
    }

    @Test
    void applyPaymentFailed_marksPendingOrderAsPaymentFailed() {
        Order order = orderRepository.save(pendingOrder());

        orderService.applyPaymentFailed(failedEventFor(order));

        assertThat(orderRepository.findById(order.getId()).orElseThrow().getStatus())
                .isEqualTo(OrderStatus.PAYMENT_FAILED);
    }

    @Test
    void applyPaymentCompleted_doesNotThrow_whenOrderDoesNotExist() {
        OrderPaymentCompletedEvent event = new OrderPaymentCompletedEvent(
                UUID.randomUUID(), UUID.randomUUID(), "test-user-id", BigDecimal.TEN, Instant.now(), BigDecimal.ONE);

        orderService.applyPaymentCompleted(event);
    }

    private static Order pendingOrder() {
        return Instancio.of(ARCHIVE_ORDER_MODEL)
                .set(field(Order::getStatus), OrderStatus.PAYMENT_PENDING)
                .create();
    }

    private static OrderPaymentCompletedEvent completedEventFor(Order order) {
        return new OrderPaymentCompletedEvent(
                UUID.randomUUID(), order.getId(), order.getUserId(), order.getPrice(), Instant.now(),
                BigDecimal.ZERO);
    }

    private static OrderPaymentFailedEvent failedEventFor(Order order) {
        return new OrderPaymentFailedEvent(
                UUID.randomUUID(), order.getId(), order.getUserId(), PaymentFailureReason.INSUFFICIENT_BALANCE,
                Instant.now());
    }

}
