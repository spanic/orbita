package com.bmstu_bureau_1440.orders.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.bmstu_bureau_1440.orders.TestContainersConfiguration;
import com.bmstu_bureau_1440.orders.dto.ArchivePayload;
import com.bmstu_bureau_1440.orders.dto.CreateOrderRequest;
import com.bmstu_bureau_1440.orders.model.Order;
import com.bmstu_bureau_1440.orders.model.OrderStatus;
import com.bmstu_bureau_1440.orders.model.OrderTypes;
import com.bmstu_bureau_1440.orders.model.SensorType;
import com.bmstu_bureau_1440.orders.repository.OrderRepository;
import com.bmstu_bureau_1440.shared.event.PaymentTopics;
import com.bmstu_bureau_1440.shared.outbox.OutboxEvent;
import com.bmstu_bureau_1440.shared.outbox.OutboxEventRepository;

@SpringBootTest
@Import(TestContainersConfiguration.class)
class OrderServiceIntegrationTest {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OutboxEventRepository outboxEventRepository;

    @BeforeEach
    void cleanUp() {
        orderRepository.deleteAll();
        outboxEventRepository.deleteAll();
    }

    @Test
    void create_setsOrderStatusToPaymentPending() {
        Order order = orderService.create("test-user-id", archiveOrderRequest());

        assertThat(order.getStatus()).isEqualTo(OrderStatus.PAYMENT_PENDING);
    }

    @Test
    void create_atomicallyWritesAnOutboxEventForOrderPaymentRequested() {
        Order order = orderService.create("test-user-id", archiveOrderRequest());

        List<OutboxEvent> events = outboxEventRepository.findAll();

        assertThat(events).hasSize(1);
        assertThat(events.getFirst().getAggregateId()).isEqualTo(order.getId());
        assertThat(events.getFirst().getEventType()).isEqualTo("ORDER_PAYMENT_REQUESTED");
        assertThat(events.getFirst().getTopic()).isEqualTo(PaymentTopics.ORDER_PAYMENT_REQUESTED);
        assertThat(events.getFirst().getPublishedAt()).isNull();
        assertThat(events.getFirst().getPayload())
                .contains(order.getId().toString())
                .contains("test-user-id");
    }

    private static CreateOrderRequest archiveOrderRequest() {
        return new CreateOrderRequest(
                OrderTypes.ARCHIVE,
                new ArchivePayload("test-aoi", LocalDate.now(), SensorType.OPTICAL));
    }

}
