package com.bmstu_bureau_1440.orders.config;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = {
        "com.bmstu_bureau_1440.orders",
        "com.bmstu_bureau_1440.shared"
})
@EnableJpaRepositories(basePackages = {
        "com.bmstu_bureau_1440.orders",
        "com.bmstu_bureau_1440.shared"
})
public class JpaConfig {
}
