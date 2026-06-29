package com.bmstu_bureau_1440.orders.repository;

import com.bmstu_bureau_1440.orders.TestContainersConfiguration;
import com.bmstu_bureau_1440.orders.model.Order;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

@SpringBootTest
@Import(TestContainersConfiguration.class)
@ExtendWith(InstancioExtension.class)
class OrdersServiceRepositoryIntegrationTest {

    private static final Model<Order> ORDER_MODEL = Instancio.of(Order.class)
            .ignore(field(Order::getId))
            .ignore(field(Order::getCreatedAt))
            .toModel();

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
        Order order = newOrder();

        Order saved = orderRepository.save(order);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCreatedAt()).isNotNull();
    }

    @Test
    void findById_returnsMatchingOrder() {
        Order saved = orderRepository.save(newOrder());

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
                .hasSize(3)
                .extracting(Order::getId)
                .doesNotContainNull();
    }

    private Order newOrder() {
        return Instancio.create(ORDER_MODEL);
    }

}
