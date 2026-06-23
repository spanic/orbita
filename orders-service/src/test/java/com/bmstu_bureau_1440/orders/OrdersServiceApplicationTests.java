package com.bmstu_bureau_1440.orders;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

/**
 * Full-context integration test. Requires a running Docker daemon
 * (Testcontainers
 * starts PostgreSQL and Kafka). Without Docker this test will fail to start its
 * containers — run the web-slice/unit tests instead.
 */
@Import(TestContainersConfiguration.class)
@SpringBootTest
class OrdersServiceApplicationTests {

    @Test
    void contextLoads() {
    }
}
