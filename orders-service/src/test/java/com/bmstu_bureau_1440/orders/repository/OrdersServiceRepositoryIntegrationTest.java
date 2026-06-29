package com.bmstu_bureau_1440.orders.repository;

import static com.bmstu_bureau_1440.orders.OrderTestsFixtures.ORDER_MODEL;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    @Test
    void save_assignsGeneratedIdAndCreatedAt() {
        Order order = Instancio.create(ORDER_MODEL);

        Order saved = orderRepository.save(order);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCreatedAt()).isNotNull();
    }

    @Test
    void findById_returnsMatchingOrder() {
        Order saved = orderRepository.save(Instancio.create(ORDER_MODEL));

        Optional<Order> found = orderRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(saved.getId());
        assertThat(found.get().getCreatedAt()).isEqualTo(saved.getCreatedAt());
    }

    @Test
    void findAll_returnsAllPersistedOrders() {
        List<Order> orders = Instancio.ofList(ORDER_MODEL).size(3).create();
        orderRepository.saveAll(orders);

        List<Order> found = orderRepository.findAll();

        assertThat(found)
                .hasSameSizeAs(orders)
                .extracting(Order::getId)
                .doesNotContainNull();
    }

}
