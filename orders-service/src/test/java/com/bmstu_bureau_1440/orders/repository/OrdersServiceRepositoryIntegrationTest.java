package com.bmstu_bureau_1440.orders.repository;

import static com.bmstu_bureau_1440.orders.OrderTestsFixtures.ARCHIVE_ORDER_MODEL;
import static com.bmstu_bureau_1440.orders.OrderTestsFixtures.MONITORING_ORDER_MODEL;
import static com.bmstu_bureau_1440.orders.OrderTestsFixtures.TASKING_ORDER_MODEL;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.bmstu_bureau_1440.orders.TestContainersConfiguration;
import com.bmstu_bureau_1440.orders.model.Order;

@SpringBootTest
@Import(TestContainersConfiguration.class)
@ExtendWith(InstancioExtension.class)
class OrdersServiceRepositoryIntegrationTest {

    @Autowired
    OrderRepository orderRepository;

    @BeforeEach
    void cleanUp() {
        orderRepository.deleteAll();
    }

    @Test
    void contextLoads() {
    }

    @ParameterizedTest
    @MethodSource("orderModels")
    void save_assignsGeneratedIdAndCreatedAt(Model<? extends Order> orderModel) {
        Order order = orderRepository.save(Instancio.create(orderModel));

        assertThat(order.getId()).isNotNull();
        assertThat(order.getCreatedAt()).isNotNull();
    }

    @ParameterizedTest
    @MethodSource("orderModels")
    void findById_returnsMatchingOrder(Model<? extends Order> orderModel) {
        Order order = orderRepository.save(Instancio.create(orderModel));

        Optional<Order> found = orderRepository.findById(order.getId());

        assertThat(found.get()).isEqualTo(order);
    }

    @ParameterizedTest
    @MethodSource("orderModels")
    void findAll_returnsAllPersistedOrders(Model<? extends Order> orderModel) {
        List<? extends Order> orders = orderRepository.saveAll(Instancio.ofList(orderModel).size(3).create());

        List<Order> found = orderRepository.findAll();

        assertThat(found)
                .hasSameSizeAs(orders)
                .containsExactlyElementsOf(orders);
    }

    private static Stream<Model<? extends Order>> orderModels() {
        return Stream.of(ARCHIVE_ORDER_MODEL, TASKING_ORDER_MODEL, MONITORING_ORDER_MODEL);
    }

}
