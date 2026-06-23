package com.bmstu_bureau_1440.orders;

import org.springframework.boot.SpringApplication;

/**
 * Run this from your IDE to start the service locally with PostgreSQL and Kafka
 * provided by Testcontainers — no manually managed infrastructure required.
 */
public class TestOrdersServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(OrdersServiceApplication::main)
                .with(TestContainersConfiguration.class)
                .run(args);
    }
}
